package zxf.camunda.saga.saga;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.stereotype.Component;
import zxf.camunda.saga.service.CamundaService;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class ByIdSaga {
    private final ProcessEngine processEngine;
    private final CamundaService camundaService;

    public String trigger(String processDefinitionId, Integer times, Integer count, Integer start) {
        start = Optional.ofNullable(start).orElse(10000);
        log.info("ById, {} trigger start, {}::{}~{}", processDefinitionId, times, start, count);
        for (int i = start; i < start + count; i++) {
            Map<String, Object> someVariables = new HashMap<>();
            someVariables.put("task-id", processDefinitionId + "@" + times + "::" + i);
            ProcessInstance processInstance = processEngine.getRuntimeService().startProcessInstanceById(processDefinitionId, someVariables);
            log.info("ById, {} instance, {}", processDefinitionId, camundaService.instanceInfo(processInstance));
        }
        log.info("ById, {} trigger end, {}::{}~{}", processDefinitionId, times, start, count);
        return String.format("%s@%d-%d~%d", processDefinitionId, times, start, count);
    }
}
