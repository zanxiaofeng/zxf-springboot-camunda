package zxf.camunda.saga.task;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import zxf.camunda.saga.util.CamundaUtils;

@Slf4j
public class CommonTask3Adapter implements JavaDelegate {

    public CommonTask3Adapter() {
        log.info("ctor()");
    }

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String taskId = (String) execution.getVariable("task-id");
        boolean isRetry = CamundaUtils.isRetry(execution);
        log.info("start, {}, {}, retry={}", taskId, execution.getId(), isRetry);

        if (taskId.endsWith("::3")) {
            log.error("Failed to process task: {}", taskId);
            throw new RuntimeException("Failed to process task: " + taskId);
            //After this, all camunda database change in this method  will be rollback(VARS...).
        }

        Thread.sleep(5000);

        log.info("end, {}, {}", taskId, execution.getId());
    }
}