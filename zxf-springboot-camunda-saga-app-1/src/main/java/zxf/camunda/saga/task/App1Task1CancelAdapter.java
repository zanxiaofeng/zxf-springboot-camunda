package zxf.camunda.saga.task;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class App1Task1CancelAdapter  implements JavaDelegate {

    public App1Task1CancelAdapter() {
        log.info("App1Task1CancelAdapter()");
    }

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        log.info("App1Task1CancelAdapter start, " + execution.getVariable("task-id"));

        Thread.sleep(20000);

        log.info("App1Task1CancelAdapter end, " + execution.getVariable("task-id"));
    }
}