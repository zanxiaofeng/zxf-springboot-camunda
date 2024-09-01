package zxf.camunda.saga.saga;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zxf.camunda.saga.base.SagaBuilder;
import zxf.camunda.saga.task.CommonTask1Adapter;
import zxf.camunda.saga.task.CommonTask2Adapter;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
public class CommonSaga {
    private final AtomicInteger counter = new AtomicInteger();
    @Autowired
    private ProcessEngine processEngine;

    @PostConstruct
    public void defineSaga() {
        try {
            String sagaName = "zxf-common";
            if (isSagaDeployed(sagaName)) {
                ProcessDefinition processDefinition = processEngine.getRepositoryService()
                        .createProcessDefinitionQuery().processDefinitionKey(sagaName).latestVersion().singleResult();
                processEngine.getManagementService()
                        .registerDeploymentForJobExecutor(processDefinition.getDeploymentId());
                log.info("zxf-common@app-2 saga had been deployed. (DeploymentId={})", processDefinition.getDeploymentId());
                return;
            }
            SagaBuilder sagaBuilder = SagaBuilder.newSaga(sagaName, true)
                    .activity("Task 1", CommonTask1Adapter.class)
                    .activity("Task 2", CommonTask2Adapter.class)
                    .end();
            Deployment deployment = processEngine.getRepositoryService().createDeployment().addModelInstance("zxf-common.bpmn", sagaBuilder.getModel()).deploy();
            log.info("zxf-common@app-2 saga deployment is done. (DeploymentId={})", deployment.getId());
        } catch (Exception ex) {
            log.error("Exception when define and deploy zxf-common@app-2 saga", ex);
        }
    }

    public void trigger(Integer count) {
        Integer times = counter.incrementAndGet();
        log.info("zxf-common@app-2 trigger start, {}::{}", times, count);
        for (int i = 0; i < count; i++) {
            Map<String, Object> someVariables = new HashMap<>();
            someVariables.put("task-id", "zxf-common@app-2@" + times + "::" + i);
            processEngine.getRuntimeService().startProcessInstanceByKey("zxf-common", someVariables);
        }
        log.info("zxf-common@app-2 trigger end, {}::{}", times, count);
    }

    private Boolean isSagaDeployed(String sageName) {
        return processEngine.getRepositoryService().createProcessDefinitionQuery()
                .processDefinitionKey(sageName).count() > 0;
    }
}
