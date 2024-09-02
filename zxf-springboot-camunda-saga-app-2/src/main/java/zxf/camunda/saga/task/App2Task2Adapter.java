package zxf.camunda.saga.task;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class App2Task2Adapter implements JavaDelegate {

    public App2Task2Adapter() {
        log.info("ctor()");
    }

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String taskId = (String) execution.getVariable("task-id");
        log.info("start, " + taskId + ", " + execution.getId());

        if (taskId.endsWith("::2")) {
            log.error("Failed to process task: " + taskId);
            throw new RuntimeException("Failed to process task: " + taskId);
        }

        Thread.sleep(20000);

        log.info("end, " + taskId + ", " + execution.getId());
    }
}