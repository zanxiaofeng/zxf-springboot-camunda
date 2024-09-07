package zxf.camunda.saga.saga;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import zxf.camunda.saga.base.SagaBuilder;
import zxf.camunda.saga.service.CamundaService;
import zxf.camunda.saga.task.CommonTask1Adapter;
import zxf.camunda.saga.task.CommonTask2Adapter;
import zxf.camunda.saga.task.CommonTask3Adapter;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
public class CommonSaga {
    private final AtomicInteger counter = new AtomicInteger();
    private final String sagaName = "zxf-common-v3.8";
    private final String eventName = sagaName + "@app-1";
    @Autowired
    private ProcessEngine processEngine;
    @Autowired
    private CamundaService camundaService;
    @Value("${saga.re-deploy}")
    private boolean sagaRedeploy;

    @PostConstruct
    public void defineSaga() {
        try {
            if (!sagaRedeploy && isSagaDeployed()) {
                ProcessDefinition processDefinition = processEngine.getRepositoryService()
                        .createProcessDefinitionQuery().processDefinitionKey(this.sagaName).latestVersion().singleResult();
                processEngine.getManagementService()
                        .registerDeploymentForJobExecutor(processDefinition.getDeploymentId());
                log.info("{} saga had been deployed. (DeploymentId={})", this.eventName, processDefinition.getDeploymentId());
                return;
            }
            SagaBuilder sagaBuilder = SagaBuilder.newSaga(this.sagaName, true)
                    .activity("Task 1", CommonTask1Adapter.class, "R3/PT0S")
                    .activityNoRetry("Task 2", CommonTask2Adapter.class)
                    .activity("Task 3", CommonTask3Adapter.class, "R6/PT5S")
                    .end();
            Deployment deployment = processEngine.getRepositoryService().createDeployment().addModelInstance(this.sagaName + ".bpmn", sagaBuilder.getModel()).deploy();
            log.info("{} saga deployment is done. (DeploymentId={})", this.eventName, deployment.getId());
        } catch (Exception ex) {
            log.error("Exception when define and deploy {} saga", this.eventName, ex);
        }
    }

    public void trigger(Integer count) {
        Integer times = counter.incrementAndGet();
        log.info("{} trigger start, {}::{}", this.eventName, times, count);
        for (int i = 0; i < count; i++) {
            Map<String, Object> someVariables = new HashMap<>();
            someVariables.put("task-id", this.eventName + "@" + times + "::" + i);
            ProcessInstance processInstance = processEngine.getRuntimeService().startProcessInstanceByKey(this.sagaName, someVariables);
            log.info("{} instance, {}", this.eventName, camundaService.instanceInfo(processInstance));
        }
        log.info("{} trigger end, {}::{}", this.eventName, times, count);
    }

    private Boolean isSagaDeployed() {
        return processEngine.getRepositoryService().createProcessDefinitionQuery()
                .processDefinitionKey(this.sagaName).count() > 0;
    }
}
