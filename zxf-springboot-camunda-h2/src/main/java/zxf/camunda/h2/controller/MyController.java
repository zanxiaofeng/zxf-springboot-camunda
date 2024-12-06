package zxf.camunda.h2.controller;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@Slf4j
@RestController
@RequestMapping("/my")
public class MyController {
    @Autowired
    ProcessEngine processEngine;

    @GetMapping("/start")
    public String startProcess(@RequestParam String caseId) {
        log.info("startProcess.");
        ProcessInstance processInstance = processEngine.getRuntimeService().startProcessInstanceByKey(
                "MyProcess", Collections.singletonMap("caseId", caseId));
        return processInstance.getProcessInstanceId();
    }

    @GetMapping("/task-complete")
    public void completeTask(@RequestParam String taskId) {
        log.info("completeTask, {}", taskId);
        processEngine.getTaskService().complete(taskId);
    }
}
