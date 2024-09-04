package zxf.camunda.saga.task;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

@Slf4j
public class App1Task1Adapter implements JavaDelegate {

    public App1Task1Adapter() {
        log.info("ctor()");
    }

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String taskId = (String) execution.getVariable("task-id");
        log.info("start, {}, {}", taskId, execution.getId());

        execution.setVariable("VAR_OF_TASK1", "var of task1");

        if (taskId.endsWith("::1")) {
            log.error("Failed to process task: {}", taskId);
            throw new RuntimeException("Failed to process task: " + taskId);
        }

        Thread.sleep(20000);

        log.info("end, {}, {}", taskId, execution.getId());
    }
}
