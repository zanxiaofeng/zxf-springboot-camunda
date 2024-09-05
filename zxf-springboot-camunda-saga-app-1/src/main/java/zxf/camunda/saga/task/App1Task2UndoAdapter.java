package zxf.camunda.saga.task;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class App1Task2UndoAdapter implements JavaDelegate {

    public App1Task2UndoAdapter() {
        log.info("ctor()");
    }

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String taskId = (String) execution.getVariable("task-id");
        log.info("start, {}, {}", taskId, execution.getId());

        Thread.sleep(20000);
        undoCreateOrder(execution, taskId);

        log.info("end, {}, {}", taskId, execution.getId());
    }

    private void undoCreateOrder(DelegateExecution execution, String taskId) {
        String orderId = (String) execution.getVariable("ORDER_ID");

        log.info("vars, taskId={}, ORDER_ID={}", taskId, orderId);
    }
}