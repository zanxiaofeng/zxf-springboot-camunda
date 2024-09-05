package zxf.camunda.saga.service;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CamundaService {
    //In order to check the first call, the camunda.bpm.default-number-of-retries must be set to a special value.
    @Value("${camunda.bpm.default-number-of-retries}")
    private int initialRetryNumber;

    public boolean isFirstExecution(DelegateExecution execution) {
        String taskId = (String) execution.getVariable("task-id");
        int totalRetryCount = getTotalRetryCount(execution);
        int leftRetryTimes = getLeftRetryTimes(execution);

        boolean isFirstExecution = leftRetryTimes == initialRetryNumber;
        log.info("Check isFirstExecution={}, {}， {}, total={}, rest={}", isFirstExecution, taskId,
                execution.getCurrentActivityName(), totalRetryCount, leftRetryTimes);
        return isFirstExecution;
    }

    public boolean isLastExecution(DelegateExecution execution) {
        String taskId = (String) execution.getVariable("task-id");
        int totalRetryCount = getTotalRetryCount(execution);
        int leftRetryTimes = getLeftRetryTimes(execution);

        boolean isLastExecution = totalRetryCount == 1 || leftRetryTimes == 1;
        log.info("Check isLastExecution={}, {}， {}, total={}, rest={}", isLastExecution, taskId,
                execution.getCurrentActivityName(), totalRetryCount, leftRetryTimes);
        return isLastExecution;
    }

    private int getLeftRetryTimes(DelegateExecution execution) {
        return execution.getProcessEngine().getManagementService().createJobQuery()
                .executionId(execution.getId()).singleResult().getRetries();
    }

    private int getTotalRetryCount(DelegateExecution execution) {
        ModelElementInstance failedJobRetryTimeCycleEle = execution.getBpmnModelElementInstance().getExtensionElements()
                .getUniqueChildElementByNameNs("http://camunda.org/schema/1.0/bpmn", "failedJobRetryTimeCycle");
        if (failedJobRetryTimeCycleEle == null) {
            return initialRetryNumber;
        }

        String failedJobRetryTimeCycle = failedJobRetryTimeCycleEle.getTextContent();
        int failedJobRetryRetryCount = Integer.parseInt(failedJobRetryTimeCycle.substring(1, failedJobRetryTimeCycle.indexOf("/")));
        return failedJobRetryRetryCount == 0 ? 1 : failedJobRetryRetryCount;
    }
}
