package zxf.camunda.saga.task;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

@Slf4j
public class CommonTask2Adapter implements JavaDelegate {

    public CommonTask2Adapter() {
        log.info("CommonTask2Adapter()");
    }

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        log.info("CommonTask2Adapter start, " + execution.getVariable("task-id"));

        Thread.sleep(4000);

        log.info("CommonTask2Adapter end, " + execution.getVariable("task-id"));
    }
}