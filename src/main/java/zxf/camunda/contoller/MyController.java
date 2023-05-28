package zxf.camunda.contoller;

import org.camunda.bpm.engine.ProcessEngine;
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

    }

    @GetMapping("/process/start/{processId}")
    public void startProcess(@PathVariable String processId) {
        processEngine.getRuntimeService().startProcessInstanceById(processId);
    }
}
