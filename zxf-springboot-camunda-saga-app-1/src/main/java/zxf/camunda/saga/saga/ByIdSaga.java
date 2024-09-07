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
import zxf.camunda.saga.task.CommonTask1Adapter;
import zxf.camunda.saga.task.CommonTask2Adapter;
import zxf.camunda.saga.task.CommonTask3Adapter;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
public class ByIdSaga {
    private final AtomicInteger counter = new AtomicInteger();
    @Autowired
    private ProcessEngine processEngine;
    @Autowired
    private CamundaService camundaService;

    public void trigger(String processDefinitionId, Integer count) {
        Integer times = counter.incrementAndGet();
        log.info("ById, {} trigger start, {}::{}", processDefinitionId, times, count);
        for (int i = 0; i < count; i++) {
            Map<String, Object> someVariables = Collections.singletonMap("task-id", processDefinitionId + "@" + times + "::" + i);
            ProcessInstance processInstance = processEngine.getRuntimeService().startProcessInstanceById(processDefinitionId, someVariables);
            log.info("ById, {} instance, {}", processDefinitionId, camundaService.instanceInfo(processInstance));
        }
        log.info("ById, {} trigger end, {}::{}", processDefinitionId, times, count);
    }
}
