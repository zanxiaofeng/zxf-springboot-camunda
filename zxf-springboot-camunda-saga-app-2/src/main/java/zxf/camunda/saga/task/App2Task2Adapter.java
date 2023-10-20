package zxf.camunda.saga.task;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class App2Task2Adapter implements JavaDelegate {

    public App2Task2Adapter() {
        log.info("App2Task2Adapter()");
    }

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        log.info("App2Task2Adapter start, " + execution.getVariable("task-id"));

        Thread.sleep(20000);

        log.info("App2Task2Adapter end, " + execution.getVariable("task-id"));
    }
}