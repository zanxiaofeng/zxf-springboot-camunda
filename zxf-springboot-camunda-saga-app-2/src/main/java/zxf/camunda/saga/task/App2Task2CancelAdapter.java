package zxf.camunda.saga.task;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class App2Task2CancelAdapter implements JavaDelegate {

    public App2Task2CancelAdapter() {
        log.info("App2Task2CancelAdapter()");
    }

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        log.info("App2Task2CancelAdapter start, " + execution.getVariable("task-id"));

        Thread.sleep(20000);

        log.info("App2Task2CancelAdapter end, " + execution.getVariable("task-id"));
    }
}