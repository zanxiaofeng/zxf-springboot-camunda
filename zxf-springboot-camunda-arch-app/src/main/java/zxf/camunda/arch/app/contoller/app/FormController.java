package zxf.camunda.arch.app.contoller.app;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.runtime.ProcessInstanceWithVariables;
import org.camunda.bpm.engine.variable.VariableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/app/form")
public class FormController {
    @Autowired
    ProcessEngine processEngine;

    @PostMapping("/start")
    public VariableMap start(@RequestBody Map<String, Object> requestBody) {
        String formId = UUID.randomUUID().toString();
        log.info("start, fromId: {}", formId);
        ProcessInstanceWithVariables processInstance = processEngine.getRuntimeService()
                .createProcessInstanceByKey("Flow-Form-Process")
                .businessKey(formId).setVariables(Collections.singletonMap("requestBody", requestBody)).executeWithVariablesInReturn();
        VariableMap returnVariables = processInstance.getVariables();
        returnVariables.put("ProcessInstanceId", processInstance.getProcessInstanceId());
        return returnVariables;
    }

    @PostMapping("/message")
    public void message(@RequestParam String formId, @RequestParam String action, Map<String, Object> messageBody) {
        log.info("message, fromId: {}, action: {}", formId, action);
        processEngine.getRuntimeService().correlateMessage("FormProcess.message", formId, Collections.singletonMap("messageBody", messageBody));
    }
}
