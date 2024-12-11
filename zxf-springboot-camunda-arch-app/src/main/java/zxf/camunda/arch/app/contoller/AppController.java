package zxf.camunda.arch.app.contoller;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import zxf.camunda.arch.app.contoller.model.ProcessParameters;

@Slf4j
@RestController
@RequestMapping("/app")
public class AppController {
    @Autowired
    ProcessEngine processEngine;

    @PostMapping("/normal-start")
    public String normalStart(@RequestParam String processKey, @RequestBody ProcessParameters processParameters) {
        log.info("normalStart, {}, {}", processKey, processParameters);
        ProcessInstance processInstance = processEngine.getRuntimeService().startProcessInstanceByKey(processKey, processParameters.getBusinessKey(), processParameters.getVariables());
        return processInstance.getProcessInstanceId();
    }

    @PostMapping("/message-start")
    public String messageStart(@RequestParam String messageId, @RequestBody ProcessParameters processParameters) {
        log.info("messageStart, {}, {}", messageId, processParameters);
        ProcessInstance processInstance = processEngine.getRuntimeService().startProcessInstanceByMessage(messageId, processParameters.getBusinessKey(), processParameters.getVariables());
        return processInstance.getProcessInstanceId();
    }

    @PostMapping("/message-received")
    public void messageReceived(@RequestParam String messageId, @RequestBody ProcessParameters processParameters) {
        log.info("messageReceived, {}, {}", messageId, processParameters);
        processEngine.getRuntimeService().correlateMessage(messageId, processParameters.getBusinessKey(), processParameters.getVariables());
    }
}
