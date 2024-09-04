package zxf.camunda.saga.task;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import zxf.camunda.saga.util.CamundaUtils;

@Slf4j
public class CommonTask1Adapter implements JavaDelegate {

    public CommonTask1Adapter() {
        log.info("ctor()");
    }

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String taskId = (String) execution.getVariable("task-id");
        log.info("start, {}, {}", taskId, execution.getId());

        CamundaUtils.checkRetry(execution);

        if (taskId.endsWith("::1")) {
            log.error("Failed to process task: {}", taskId);
            throw new RuntimeException("Failed to process task: " + taskId);
            //After this, all camunda database change in this method  will be rollback(VARS...).
        }

        Thread.sleep(20000);

        log.info("end, {}, {}", taskId, execution.getId());
    }
}