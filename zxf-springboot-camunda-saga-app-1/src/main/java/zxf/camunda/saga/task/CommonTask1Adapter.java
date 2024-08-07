package zxf.camunda.saga.task;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

@Slf4j
public class CommonTask1Adapter implements JavaDelegate {

    public CommonTask1Adapter() {
        log.info("CommonTask1Adapter()");
    }

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        log.info("CommonTask1Adapter start, " + execution.getVariable("task-id"));

        Thread.sleep(4000);

        log.info("CommonTask1Adapter end, " + execution.getVariable("task-id"));
    }
}