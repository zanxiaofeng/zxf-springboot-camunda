package zxf.camunda.saga.util;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.runtime.Job;

@Slf4j
public class CamundaUtils {
    public static boolean isRetry(DelegateExecution execution) {
        String taskId = (String) execution.getVariable("task-id");
        int leftRetryTimes = execution.getProcessEngine().getManagementService().createJobQuery()
                .executionId(execution.getId()).singleResult().getRetries();
        //In order to check the first call, the camunda.bmp.default-number-of-retries must be set to 11.
        boolean isFirstExecution = leftRetryTimes == 11;
        boolean isRetry = !isFirstExecution && leftRetryTimes > 0;
        log.info("check retry, {}， {}, left={}, isRetry={}", taskId, execution.getCurrentActivityName(), leftRetryTimes, isRetry);
        return isRetry;
    }
}
