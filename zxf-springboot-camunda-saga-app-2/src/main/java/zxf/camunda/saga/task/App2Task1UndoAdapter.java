package zxf.camunda.saga.task;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class App2Task1UndoAdapter implements JavaDelegate {

    public App2Task1UndoAdapter() {
        log.info("ctor()");
    }

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String taskId = (String) execution.getVariable("task-id");
        log.info("start, {}, {}", taskId, execution.getId());

        String varOfTask1 = (String) execution.getVariable("VAR_OF_TASK1");
        String varOfTask2 = (String) execution.getVariable("VAR_OF_TASK2");
        String varOfTask3 = (String) execution.getVariable("VAR_OF_TASK3");

        log.info("vars, taskId={}, VAR_OF_TASK1={}, VAR_OF_TASK2={}, VAR_OF_TASK3={}", taskId, varOfTask1, varOfTask2, varOfTask3);

        Thread.sleep(20000);

        log.info("end, {}, {}", taskId, execution.getId());
    }
}