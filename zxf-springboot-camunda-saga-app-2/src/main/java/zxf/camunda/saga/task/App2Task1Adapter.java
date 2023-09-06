package zxf.camunda.saga.task;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

@Slf4j
public class App2Task1Adapter implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        log.info("App2Task1Adapter start, " + execution.getVariable("task-id"));

        Thread.sleep(20000);

        log.info("App2Task1Adapter end, " + execution.getVariable("task-id"));
    }
}
