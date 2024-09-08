package zxf.camunda.saga.task;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zxf.camunda.saga.service.CamundaService;

@Slf4j
@Component
public class App1Task3Adapter implements JavaDelegate {

    public App1Task3Adapter() {
        log.info("ctor()");
    }

    @Autowired
    private CamundaService camundaService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String taskId = (String) execution.getVariable("task-id");
        log.info("start, {}", camundaService.taskInfo(execution));

        orderServerC(execution, taskId);
        Thread.sleep(3000);

        log.info("end  , {}", camundaService.taskInfo(execution));
    }

    private static void orderServerC(DelegateExecution execution, String taskId) {
        execution.setVariable("VAR_OF_TASK3", "var of task3");

        if (taskId.endsWith("::3000")) {
            log.error("Failed to process task: {}", taskId);
            throw new RuntimeException("Failed to process task: " + taskId);
        }
    }
}