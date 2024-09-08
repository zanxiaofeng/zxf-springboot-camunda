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
import zxf.camunda.saga.base.SagaBuilder;
import zxf.camunda.saga.service.CamundaService;
import zxf.camunda.saga.task.*;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class CommonSaga {
    private final String sagaName = "zxf-common-v3.8";
    private final String eventName = sagaName + "@app-2";
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

    public void trigger(String prefix, Integer times, Integer count) {
        log.info("{} trigger start, {}::{}", prefix, times, count);
        for (int i = 0; i < count; i++) {
            String taskId = prefix + "#" + times + "::" + i;
            Map<String, Object> someVariables = new HashMap<>();
            someVariables.put("task-id", taskId);
            //This method will always create instance base on the latest version.
            ProcessInstance processInstance = processEngine.getRuntimeService().startProcessInstanceByKey(this.sagaName, taskId, someVariables);
            log.info("{} instance, {}", prefix, camundaService.instanceInfo(processInstance));
        }
        log.info("{} trigger end, {}::{}", prefix, times, count);
    }

    private BpmnModelInstance buildSaga() {
        SagaBuilder sagaBuilder = SagaBuilder.newSaga(this.sagaName, true)
                .activity("Task 1", CommonTask1Adapter.class, "R3/PT0S")
                .activityNoRetry("Task 2", CommonTask2Adapter.class)
                .activity("Task 3", CommonTask3Adapter.class, "R3/PT5S")
                .end();
        return sagaBuilder.getModel();
    }

    private Boolean isSagaDeployed() {
        Boolean hadBeenDeployed = processEngine.getRepositoryService().createProcessDefinitionQuery()
                .processDefinitionKey(this.sagaName).count() > 0;
        log.info("{} isSagaDeployed, {}", this.eventName, hadBeenDeployed);
        return hadBeenDeployed;
    }
}
