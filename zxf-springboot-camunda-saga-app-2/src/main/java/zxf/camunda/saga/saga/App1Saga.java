package zxf.camunda.saga.saga;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zxf.camunda.saga.service.CamundaService;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class App1Saga {
    private final String sagaName = "zxf-app-1-v2";
    @Autowired
    private ProcessEngine processEngine;
    @Autowired
    private CamundaService camundaService;

    public void trigger(String prefix, Integer times, Integer count) {
        log.info("{} trigger start, {}::{}", prefix, times, count);
        for (int i = 0; i < count; i++) {
            Map<String, Object> someVariables = new HashMap<>();
            someVariables.put("task-id", prefix + "#" + times + "::" + i);
            //This method will always create instance base on the latest version.
            ProcessInstance processInstance = processEngine.getRuntimeService().startProcessInstanceByKey(this.sagaName, someVariables);
            log.info("{} instance, {}", prefix, camundaService.instanceInfo(processInstance));
        }
        log.info("{} trigger end, {}::{}", prefix, times, count);
    }
}
