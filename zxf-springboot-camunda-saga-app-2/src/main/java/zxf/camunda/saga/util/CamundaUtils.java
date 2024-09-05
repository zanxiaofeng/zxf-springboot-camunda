package zxf.camunda.saga.util;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;

@Slf4j
public class CamundaUtils {
    public static boolean isRetry(DelegateExecution execution, int totalRetryTimes) {
        String taskId = (String) execution.getVariable("task-id");
        int leftRetryTimes = execution.getProcessEngine().getManagementService().createJobQuery()
                .executionId(execution.getId()).singleResult().getRetries();
        log.info("check retry, {}， {}, total={}, left={}", taskId, execution.getCurrentActivityName(), totalRetryTimes, leftRetryTimes);
        return totalRetryTimes - leftRetryTimes > 0;
    }
}
