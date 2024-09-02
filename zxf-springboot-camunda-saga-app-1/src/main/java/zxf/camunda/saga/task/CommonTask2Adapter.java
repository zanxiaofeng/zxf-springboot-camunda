package zxf.camunda.saga.task;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

@Slf4j
public class CommonTask2Adapter implements JavaDelegate {

    public CommonTask2Adapter() {
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