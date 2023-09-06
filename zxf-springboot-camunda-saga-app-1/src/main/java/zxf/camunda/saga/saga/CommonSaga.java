package zxf.camunda.saga.saga;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.ProcessEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zxf.camunda.saga.base.SagaBuilder;
import zxf.camunda.saga.task.*;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class CommonSaga {
    @Autowired
    private ProcessEngine processEngine;

    @PostConstruct
    public void defineSaga() {
        try {
            SagaBuilder sagaBuilder = new SagaBuilder("zxf-common-1").start().activity("Task 1", CommonTask1Adapter.class).activity("Task 2", CommonTask2Adapter.class).end();
            processEngine.getRepositoryService().createDeployment().addModelInstance("zxf-common-1.bpmn", sagaBuilder.getModel()).deploy();
        } catch (Exception ex) {
            log.error("Exception when define and deploy zxf-common-1 saga", ex);
        }
    }

    public void trigger(Integer count) {
        for (int i = 0; i < count; i++) {
            Map<String, Object> someVariables = new HashMap<>();
            someVariables.put("task-id", "zxf-common-1-app-1-" + i);
            processEngine.getRuntimeService().startProcessInstanceByKey("zxf-common-1", someVariables);
        }
    }
}
