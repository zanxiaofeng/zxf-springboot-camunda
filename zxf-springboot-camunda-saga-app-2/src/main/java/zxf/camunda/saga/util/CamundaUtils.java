package zxf.camunda.saga.util;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;

@Slf4j
public class CamundaUtils {
    public static void checkRetry(DelegateExecution execution) {
        int retry = execution.getProcessEngine().getManagementService().createJobQuery().executionId(execution.getId()).singleResult().getRetries();
        log.info("check retry: {}, {}", execution.getVariable("task-id"), retry);
    }
}
