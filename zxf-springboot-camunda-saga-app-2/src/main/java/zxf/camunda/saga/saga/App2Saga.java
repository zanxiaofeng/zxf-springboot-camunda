package zxf.camunda.saga.saga;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.ProcessEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zxf.camunda.saga.base.SagaBuilder;
import zxf.camunda.saga.task.App2Task1Adapter;
import zxf.camunda.saga.task.App2Task1CancelAdapter;
import zxf.camunda.saga.task.App2Task2Adapter;
import zxf.camunda.saga.task.App2Task2CancelAdapter;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class App2Saga {
    @Autowired
    private ProcessEngine processEngine;

    @PostConstruct
    public void defineSaga() {
        try {
            SagaBuilder sagaBuilder = new SagaBuilder("zxf-app-2").start().activity("Task 1", App2Task1Adapter.class).compensationActivity("Cancel Task 1", App2Task1CancelAdapter.class).activity("Task 2", App2Task2Adapter.class).compensationActivity("Cancel Task 2", App2Task2CancelAdapter.class).end().triggerCompensationOnAnyError();
            processEngine.getRepositoryService().createDeployment().addModelInstance("zxf-app-2.bpmn", sagaBuilder.getModel()).deploy();
            log.info("zxf-app-2 saga has been deployed");
        } catch (Exception ex) {
            log.error("Exception when define and deploy zxf-app-2 saga", ex);
        }
    }

    public void trigger(Integer count) {
        log.info("zxf-app-2 trigger start, " + count);
        for (int i = 0; i < count; i++) {
            Map<String, Object> someVariables = new HashMap<>();
            someVariables.put("task-id", "zxf-app-2-" + i);
            processEngine.getRuntimeService().startProcessInstanceByKey("zxf-app-2", someVariables);
        }
        log.info("zxf-app-2 trigger end, " + count);
    }

}
