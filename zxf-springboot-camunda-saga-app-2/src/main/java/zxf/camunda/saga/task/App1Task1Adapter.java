package zxf.camunda.saga.task;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zxf.camunda.saga.service.CamundaService;

@Slf4j
@Component
public class App1Task1Adapter implements JavaDelegate {

    public App1Task1Adapter() {
        log.info("ctor()");
    }

    @Autowired
    private CamundaService camundaService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String taskId = (String) execution.getVariable("task-id");
        log.info("start, {}", camundaService.taskInfo(execution));

        orderServerA(execution, taskId);
        Thread.sleep(3000);

        log.info("end  , {}", camundaService.taskInfo(execution));
    }

    private static void orderServerA(DelegateExecution execution, String taskId) {
        execution.setVariable("VAR_OF_TASK1", "var of task1");

        if (taskId.endsWith("::1000")) {
            log.error("Failed to process task: {}", taskId);
            throw new RuntimeException("Failed to process task: " + taskId);
        }
    }
}
