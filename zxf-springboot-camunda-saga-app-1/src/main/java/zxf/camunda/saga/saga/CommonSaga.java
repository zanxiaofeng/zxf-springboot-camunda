package zxf.camunda.saga.saga;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zxf.camunda.saga.base.SagaBuilder;
import zxf.camunda.saga.task.CommonTask1Adapter;
import zxf.camunda.saga.task.CommonTask2Adapter;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
public class CommonSaga {
    private AtomicInteger counter = new AtomicInteger();
    private final String sagaName = "zxf-common";
    @Autowired
    private ProcessEngine processEngine;

    @PostConstruct
    public void defineSaga() {
        try {
            if (isSagaDeployed()) {
                ProcessDefinition processDefinition = processEngine.getRepositoryService()
                        .createProcessDefinitionQuery().processDefinitionKey(this.sagaName).latestVersion().singleResult();
                processEngine.getManagementService()
                        .registerDeploymentForJobExecutor(processDefinition.getDeploymentId());
                log.info("{}@app-1 saga had been deployed. (DeploymentId={})", this.sagaName, processDefinition.getDeploymentId());
                return;
            }
            SagaBuilder sagaBuilder = SagaBuilder.newSaga(sagaName, true)
                    .activity("Task 1", CommonTask1Adapter.class)
                    .activity("Task 2", CommonTask2Adapter.class)
                    .end();
            Deployment deployment = processEngine.getRepositoryService().createDeployment().addModelInstance(this.sagaName + ".bpmn", sagaBuilder.getModel()).deploy();
            log.info("{}@app-1 saga deployment is done. (DeploymentId={})", this.sagaName, deployment.getId());
        } catch (Exception ex) {
            log.error("Exception when define and deploy {}@app-1 saga", this.sagaName, ex);
        }
    }

    public void trigger(Integer count) {
        Integer times = counter.incrementAndGet();
        log.info("{}@app-1 trigger start, {}::{}", this.sagaName, times, count);
        for (int i = 0; i < count; i++) {
            Map<String, Object> someVariables = new HashMap<>();
            someVariables.put("task-id", this.sagaName + "@app-1@" + times + "::" + i);
            processEngine.getRuntimeService().startProcessInstanceByKey(this.sagaName, someVariables);
        }
        log.info("{}@app-1 trigger end, {}::{}", this.sagaName, times, count);
    }

    private Boolean isSagaDeployed() {
        return processEngine.getRepositoryService().createProcessDefinitionQuery()
                .processDefinitionKey(this.sagaName).count() > 0;
    }
}
