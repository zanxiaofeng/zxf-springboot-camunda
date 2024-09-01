package zxf.camunda.saga.task;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class App2Task1CancelAdapter implements JavaDelegate {

    public App2Task1CancelAdapter() {
        log.info("ctor()");
    }

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String taskId = (String) execution.getVariable("task-id");
        log.info("start, " + taskId + ", " + execution.getId());

        Thread.sleep(20000);

        log.info("end, " + taskId + ", " + execution.getId());
    }
}