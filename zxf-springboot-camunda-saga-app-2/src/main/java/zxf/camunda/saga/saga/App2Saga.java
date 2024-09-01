package zxf.camunda.saga.saga;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zxf.camunda.saga.base.SagaBuilder;
import zxf.camunda.saga.task.App2Task1Adapter;
import zxf.camunda.saga.task.App2Task1CancelAdapter;
import zxf.camunda.saga.task.App2Task2Adapter;
import zxf.camunda.saga.task.App2Task2CancelAdapter;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
public class App2Saga {
    private final AtomicInteger counter = new AtomicInteger();
    @Autowired
    private ProcessEngine processEngine;

    @PostConstruct
    public void defineSaga() {
        try {
            String sagaName = "zxf-app-2";
            if (isSagaDeployed(sagaName)) {
                ProcessDefinition processDefinition = processEngine.getRepositoryService()
                        .createProcessDefinitionQuery().processDefinitionKey(sagaName).latestVersion().singleResult();
                processEngine.getManagementService()
                        .registerDeploymentForJobExecutor(processDefinition.getDeploymentId());
                log.info("zxf-app-2 saga had been deployed. (DeploymentId={})", processDefinition.getDeploymentId());
                return;
            }
            SagaBuilder sagaBuilder = SagaBuilder.newSaga(sagaName, true)
                    .activityWithoutRetry("Task 1", App2Task1Adapter.class)
                    .compensationActivity("Cancel Task 1", App2Task1CancelAdapter.class)
                    .activityWithoutRetry("Task 2", App2Task2Adapter.class)
                    .compensationActivity("Cancel Task 2", App2Task2CancelAdapter.class)
                    .end()
                    .triggerCompensationOnAnyError();
            Deployment deployment = processEngine.getRepositoryService().createDeployment().addModelInstance("zxf-app-2.bpmn", sagaBuilder.getModel()).deploy();
            log.info("zxf-app-2 saga deployment is done. (DeploymentId={})", deployment.getId());
        } catch (Exception ex) {
            log.error("Exception when define and deploy zxf-app-2 saga", ex);
        }
    }

    public void trigger(Integer count) {
        Integer times = counter.incrementAndGet();
        log.info("zxf-app-2 trigger start, {}::{}", times, count);
        for (int i = 0; i < count; i++) {
            Map<String, Object> someVariables = new HashMap<>();
            someVariables.put("task-id", "zxf-app-2@" + times + "::" + i);
            processEngine.getRuntimeService().startProcessInstanceByKey("zxf-app-2", someVariables);
        }
        log.info("zxf-app-2 trigger end, {}::{}", times, count);
    }

    private Boolean isSagaDeployed(String sageName) {
        return processEngine.getRepositoryService().createProcessDefinitionQuery()
                .processDefinitionKey(sageName).count() > 0;
    }
}
