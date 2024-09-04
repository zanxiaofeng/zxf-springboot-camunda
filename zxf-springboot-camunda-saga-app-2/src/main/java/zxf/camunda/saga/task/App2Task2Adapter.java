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
        log.info("start, {}, {}", taskId, execution.getId());

        Thread.sleep(20000);
        createOrder(execution, taskId);

        log.info("end, {}, {}", taskId, execution.getId());
    }

    private void createOrder(DelegateExecution execution, String taskId) {
        execution.setVariable("VAR_OF_TASK2", "var of task2");

        if (taskId.endsWith("::2")) {
            log.error("Failed to process task: {}", taskId);
            throw new RuntimeException("Failed to process task: " + taskId);
        }
    }
}