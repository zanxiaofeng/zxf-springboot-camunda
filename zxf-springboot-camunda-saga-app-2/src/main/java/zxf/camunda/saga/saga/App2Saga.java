package zxf.camunda.saga.saga;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import zxf.camunda.saga.base.SagaBuilder;
import zxf.camunda.saga.service.CamundaService;
import zxf.camunda.saga.task.*;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
public class App2Saga {
    private final AtomicInteger counter = new AtomicInteger();
    private final String sagaName = "zxf-app-2-v4";
    private final String eventName = sagaName;
    @Autowired
    private ProcessEngine processEngine;
    @Autowired
    private CamundaService camundaService;
    @Value("${saga.re-deploy}")
    private boolean sagaRedeploy;
    @Value("${camunda.bpm.job-execution.deployment-aware}")
    private boolean deploymentAware;

    @PostConstruct
    public void defineSaga() {
        try {
            if (sagaRedeploy || !isSagaDeployed()) {
                BpmnModelInstance bpmnModelInstance = buildSaga();
                Deployment deployment = processEngine.getRepositoryService().createDeployment()
                        .addModelInstance(this.sagaName + ".bpmn", bpmnModelInstance).deploy();
                log.info("{} saga deployment is done. (DeploymentId={})", this.eventName, deployment.getId());
                return;
            }

            if (deploymentAware) {
                ProcessDefinition processDefinition = processEngine.getRepositoryService()
                        .createProcessDefinitionQuery().processDefinitionKey(this.sagaName).latestVersion().singleResult();
                processEngine.getManagementService()
                        .registerDeploymentForJobExecutor(processDefinition.getDeploymentId());
                log.info("{} saga had been deployed, register job executor to the latest deployment. (DeploymentId={})", this.eventName, processDefinition.getDeploymentId());
            }

        } catch (Exception ex) {
            log.error("Exception when deploy {} saga or register job executor", this.eventName, ex);
        }
    }

    public void trigger(String processDefinitionId, Integer count) {
        Integer times = counter.incrementAndGet();
        log.info("{} trigger start, {}::{}", this.eventName, times, count);
        for (int i = 0; i < count; i++) {
            Map<String, Object> someVariables = Collections.singletonMap("task-id", this.eventName + "@" + times + "::" + i);
            ProcessInstance processInstance = null;
            if (StringUtils.isEmpty(processDefinitionId)) {
                //This method will always create instance base on the latest version.
                processInstance = processEngine.getRuntimeService().startProcessInstanceByKey(this.sagaName, someVariables);
            } else {
                processInstance = processEngine.getRuntimeService().startProcessInstanceById(processDefinitionId, someVariables);
            }
            log.info("{} instance, {}", this.eventName, camundaService.instanceInfo(processInstance));
        }
        log.info("{} trigger end, {}::{}", this.eventName, times, count);
    }

    private BpmnModelInstance buildSaga() {
        SagaBuilder sagaBuilder = SagaBuilder.newSaga(this.sagaName, true)
                .activityNoRetry("Task 1", App2Task1Adapter.class)
                .compensationActivity("Undo Task 1", App2Task1UndoAdapter.class)
                .activityNoRetry("Task 2", App2Task2Adapter.class)
                .compensationActivity("Undo Task 2", App2Task2UndoAdapter.class)
                .activityNoRetry("Task 3", App2Task3Adapter.class)
                .end()
                .triggerCompensationActivityOnAnyError("Finally Undo", App2TaskEndUndoAdapter.class);
        //Undo flow: Undo Task 2 --> Undo Task 1 --> Finally Undo
        return sagaBuilder.getModel();
    }

    private Boolean isSagaDeployed() {
        return processEngine.getRepositoryService().createProcessDefinitionQuery()
                .processDefinitionKey(this.sagaName).count() > 0;
    }
}
