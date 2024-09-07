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
import zxf.camunda.saga.task.App2Task1Adapter;
import zxf.camunda.saga.task.App2Task1UndoAdapter;

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

    public void trigger(Integer count) {
        Integer times = counter.incrementAndGet();
        log.info("{} trigger start, {}::{}", this.eventName, times, count);
        for (int i = 0; i < count; i++) {
            Map<String, Object> someVariables = Collections.singletonMap("task-id", this.eventName + "@" + times + "::" + i);
            //This method will always create instance base on the latest version.
            ProcessInstance processInstance = processEngine.getRuntimeService().startProcessInstanceByKey(this.sagaName, someVariables);
            log.info("{} instance, {}", this.eventName, camundaService.instanceInfo(processInstance));
        }
        log.info("{} trigger end, {}::{}", this.eventName, times, count);
    }
}
