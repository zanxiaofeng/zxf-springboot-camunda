package zxf.camunda.saga.service;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class CamundaService {
    //In order to check the first call, the camunda.bpm.default-number-of-retries must be set to a large number.
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
        int leftRetryTimes = execution.getProcessEngine().getManagementService().createJobQuery()
                .executionId(execution.getId()).singleResult().getRetries();
        if (leftRetryTimes > initialRetryNumber) {
            throw new IllegalStateException(String.format("The default retry number must be set to a large number. initial=%d, left=%d", initialRetryNumber, leftRetryTimes));
        }
        return leftRetryTimes;
    }

    private int getTotalRetryCount(DelegateExecution execution) {
        Optional<Integer> failedJobRetryCount = Optional.ofNullable(execution.getBpmnModelElementInstance().getExtensionElements())
                .map(ele -> ele.getUniqueChildElementByNameNs("http://camunda.org/schema/1.0/bpmn", "failedJobRetryTimeCycle"))
                .map(ModelElementInstance::getTextContent)
                .map(this::parseRetryTimes);
        int totalRetryCount = failedJobRetryCount.map(failedRetryCount -> failedRetryCount == 0 ? 1 : failedRetryCount).orElseGet(() -> initialRetryNumber);
        if (totalRetryCount > initialRetryNumber) {
            throw new IllegalStateException(String.format("The default retry number must be set to a large number. initial=%d, total=%d", initialRetryNumber, totalRetryCount));
        }
        return totalRetryCount;
    }

    private Integer parseRetryTimes(String failedJobRetryTimeCycle) {
        return Integer.parseInt(failedJobRetryTimeCycle.substring(1, failedJobRetryTimeCycle.indexOf("/")));
    }
}
