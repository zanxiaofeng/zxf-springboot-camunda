package zxf.camunda.saga.saga;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.ProcessEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zxf.camunda.saga.base.SagaBuilder;
import zxf.camunda.saga.task.App1Task1Adapter;
import zxf.camunda.saga.task.App1Task1CancelAdapter;
import zxf.camunda.saga.task.App1Task2Adapter;
import zxf.camunda.saga.task.App1Task2CancelAdapter;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class App1Saga {
    @Autowired
    private ProcessEngine processEngine;

    @PostConstruct
    public void defineSaga() {
        try {
            SagaBuilder sagaBuilder = new SagaBuilder("zxf-app-1").start().activity("Task 1", App1Task1Adapter.class).compensationActivity("Cancel Task 1", App1Task1CancelAdapter.class).activity("Task 2", App1Task2Adapter.class).compensationActivity("Cancel Task 2", App1Task2CancelAdapter.class).end().triggerCompensationOnAnyError();
            processEngine.getRepositoryService().createDeployment().addModelInstance("zxf-app-1.bpmn", sagaBuilder.getModel()).deploy();
            log.info("zxf-app-1 saga has been deployed");
        } catch (Exception ex) {
            log.error("Exception when define and deploy zxf-app-1 saga", ex);
        }
    }

    public void trigger(Integer count) {
        log.info("zxf-app-1 trigger start, " + count);
        for (int i = 0; i < count; i++) {
            Map<String, Object> someVariables = new HashMap<>();
            someVariables.put("task-id", "zxf-app-1-" + i);
            processEngine.getRuntimeService().startProcessInstanceByKey("zxf-app-1", someVariables);
        }
        log.info("zxf-app-1 trigger end, " + count);
    }

}
