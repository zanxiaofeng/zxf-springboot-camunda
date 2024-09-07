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
import zxf.camunda.saga.task.*;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
public class App2Saga {
    private final AtomicInteger counter = new AtomicInteger();
    private final String sagaName = "zxf-app-2-v3.5";
    @Autowired
    private ProcessEngine processEngine;
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
                log.info("{} saga had been deployed. (DeploymentId={})", this.sagaName, processDefinition.getDeploymentId());
                return;
            }
            SagaBuilder sagaBuilder = SagaBuilder.newSaga(this.sagaName, true)
                    .activityNoRetry("Task 1", App2Task1Adapter.class)
                    .compensationActivity("Undo Task 1", App2Task1UndoAdapter.class)
                    .activityNoRetry("Task 2", App2Task2Adapter.class)
                    .compensationActivity("Undo Task 2", App2Task2UndoAdapter.class)
                    .activityNoRetry("Task 3", App2Task3Adapter.class)
                    .end()
                    .triggerCompensationActivityOnAnyError("Finally Undo", App2TaskEndUndoAdapter.class);
            //Undo flow: Undo Task 2 --> Undo Task 1 --> Finally Undo
            Deployment deployment = processEngine.getRepositoryService().createDeployment().addModelInstance(this.sagaName + ".bpmn", sagaBuilder.getModel()).deploy();
            log.info("{} saga deployment is done. (DeploymentId={})", this.sagaName, deployment.getId());
        } catch (Exception ex) {
            log.error("Exception when define and deploy {} saga", this.sagaName, ex);
        }
    }

    public void trigger(Integer count) {
        Integer times = counter.incrementAndGet();
        log.info("{} trigger start, {}::{}", this.sagaName, times, count);
        for (int i = 0; i < count; i++) {
            Map<String, Object> someVariables = new HashMap<>();
            someVariables.put("task-id", this.sagaName + "@" + times + "::" + i);
            ProcessInstance processInstance = processEngine.getRuntimeService()
                    .startProcessInstanceByKey(this.sagaName, someVariables);
            logProcessInstance(processInstance);
        }
        log.info("{} trigger end, {}::{}", this.sagaName, times, count);
    }

    private Boolean isSagaDeployed() {
        return processEngine.getRepositoryService().createProcessDefinitionQuery()
                .processDefinitionKey(this.sagaName).count() > 0;
    }

    private void logProcessInstance(ProcessInstance processInstance) {
        log.info("{} instance, {}::{}", this.sagaName, processInstance.getProcessInstanceId(), processInstance.getProcessDefinitionId());
    }
}
