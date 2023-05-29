package zxf.camunda.contoller;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyController {
    @Autowired
    ProcessEngine processEngine;

    @GetMapping("/process/deploy/{processId}")
    public void deployProcess(@PathVariable String processId) {
//        Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
//        taskService.complete(task.getId());
//        logger.info("completed task: {}", task);
    }

    @GetMapping("/process/start/{processId}")
    public ProcessInstance startProcess(@PathVariable String processId) {
        ProcessInstance processInstance = processEngine.getRuntimeService().startProcessInstanceByKey("loanRequest");
        return processInstance;
    }

}
