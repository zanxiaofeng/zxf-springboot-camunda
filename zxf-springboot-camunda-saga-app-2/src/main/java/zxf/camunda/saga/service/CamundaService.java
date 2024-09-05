package zxf.camunda.saga.service;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CamundaService {
    @Value("${camunda.bmp.default-number-of-retries}")
    private int defaultNumberOfRetries;

    public boolean isFirstExecution(DelegateExecution execution) {
        String taskId = (String) execution.getVariable("task-id");
        int leftRetryTimes = execution.getProcessEngine().getManagementService().createJobQuery()
                .executionId(execution.getId()).singleResult().getRetries();
        //In order to check the first call, the camunda.bmp.default-number-of-retries must be set to a special value.
        boolean isFirstExecution = leftRetryTimes == defaultNumberOfRetries;
        log.info("Check isFirstExecution={}, {}， {}, rest={}", isFirstExecution, taskId,
                execution.getCurrentActivityName(), leftRetryTimes);
        return isFirstExecution;
    }
}
