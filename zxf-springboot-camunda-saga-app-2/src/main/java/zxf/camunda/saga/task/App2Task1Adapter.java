package zxf.camunda.saga.task;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

@Slf4j
public class App2Task1Adapter implements JavaDelegate {

    public App2Task1Adapter() {
        log.info("App2Task1Adapter()");
    }

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String taskId = (String) execution.getVariable("task-id");
        log.info("App2Task1Adapter start, " + taskId);

        if (taskId.endsWith("::1")) {
            log.error("App2Task1Adapter Failed to process task: " + taskId);
            throw new RuntimeException("Failed to process task: " + taskId);
        }

        Thread.sleep(20000);

        log.info("App2Task1Adapter end, " + taskId);
    }
}
