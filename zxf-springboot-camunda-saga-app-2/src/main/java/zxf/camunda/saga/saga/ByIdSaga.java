package zxf.camunda.saga.saga;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zxf.camunda.saga.service.CamundaService;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class ByIdSaga {
    @Autowired
    private ProcessEngine processEngine;
    @Autowired
    private CamundaService camundaService;

    public void trigger(String processDefinitionId, Integer times, Integer count) {
        log.info("ById, {} trigger start, {}::{}", processDefinitionId, times, count);
        for (int i = 0; i < count; i++) {
            Map<String, Object> someVariables = new HashMap<>();
            someVariables.put("task-id", processDefinitionId + "@" + times + "::" + i);
            ProcessInstance processInstance = processEngine.getRuntimeService().startProcessInstanceById(processDefinitionId, someVariables);
            log.info("ById, {} instance, {}", processDefinitionId, camundaService.instanceInfo(processInstance));
        }
        log.info("ById, {} trigger end, {}::{}", processDefinitionId, times, count);
    }
}
