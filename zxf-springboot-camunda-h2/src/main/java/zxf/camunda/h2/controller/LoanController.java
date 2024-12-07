package zxf.camunda.h2.controller;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/loan")
public class LoanController {
    @Autowired
    ProcessEngine processEngine;

    @GetMapping("/request")
    public String loadRequest(@RequestParam Integer amount) {
        log.info("loadRequest, {}", amount);
        Map<String, Object> variables = new HashMap<>();
        variables.put("amount", amount);
        ProcessInstance processInstance = processEngine.getRuntimeService().startProcessInstanceByKey("LoanProcess", variables);
        return processInstance.getProcessInstanceId();
    }

    @GetMapping("/approve")
    public void loadApprove(@RequestParam String taskId, @RequestParam Integer amount) {
        log.info("loadApprove, {}", taskId);
        Map<String, Object> variables = new HashMap<>();
        variables.put("approvalAmount", amount);
        processEngine.getTaskService().complete(taskId, variables);
    }

    @GetMapping("/updated")
    public void loadUpdated(@RequestParam String executionId, @RequestParam Integer amount) {
        log.info("loadUpdated, {}", amount);
        Map<String, Object> variables = new HashMap<>();
        variables.put("amount", amount);
        processEngine.getRuntimeService().messageEventReceived("LoanProcess.LoanRequestUpdated", executionId, variables);
    }
}
