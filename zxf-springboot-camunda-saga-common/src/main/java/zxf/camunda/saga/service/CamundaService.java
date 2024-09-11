package zxf.camunda.saga.service;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.ManagementService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.management.JobDefinition;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.Job;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

@Slf4j
@Component
public class CamundaService {
    //In order to check the first call, the camunda.bpm.default-number-of-retries must be set to a large number.
    @Value("${camunda.bpm.default-number-of-retries}")
    private int initialRetryNumber;

    @Value("${saga.re-deploy}")
    private boolean sagaRedeploy;
    @Value("${saga.register-deployment}")
    private boolean registerDeployment;

    @Value("${saga.throw-exception}")
    private boolean throwException;

    @Autowired
    private ProcessEngine processEngine;

    public boolean sagaRedeploy() {
        return sagaRedeploy;
    }

    public boolean registerDeployment() {
        return throwException;
    }

    public boolean throwException() {
        return throwException;
    }


    public boolean isFirstExecution(DelegateExecution execution) {
        int totalRetryCount = getTotalRetryCount(execution);
        int leftRetryTimes = getLeftRetryTimes(execution);

        boolean isFirstExecution = leftRetryTimes == initialRetryNumber;
        log.info("check, firstCall={}, total={}, rest={}, {}", isFirstExecution, totalRetryCount, leftRetryTimes, taskInfo(execution));
        return isFirstExecution;
    }

    public boolean isLastExecution(DelegateExecution execution) {
        int totalRetryCount = getTotalRetryCount(execution);
        int leftRetryTimes = getLeftRetryTimes(execution);

        boolean isLastExecution = totalRetryCount == 1 || leftRetryTimes == 1;
        log.info("check, lastCall={}, total={}, rest={}, {}", isLastExecution, totalRetryCount, leftRetryTimes, taskInfo(execution));
        return isLastExecution;
    }

    public String instanceInfo(ProcessInstance instance) {
        return String.format("(ProcessInstanceId=%s, %s, ProcessDefinitionId=%s, BusinessKey=%s, CaseInstanceId=%s, isSuspended=%s, TenantId=%s)"
                , instance.getProcessInstanceId(), instance.getRootProcessInstanceId(), instance.getProcessDefinitionId(),
                instance.getBusinessKey(), instance.getCaseInstanceId(), instance.isSuspended(), instance.getTenantId());
    }

    public String definitionInfo(ProcessDefinition definition) {
        return String.format("(Id=%s, Category=%s, Key=%s, Version=%s, ResourceName=%s, DeploymentId=%s, isSuspended=%s, HistoryTimeToLive=%d, TenantId=%s)",
                definition.getId(), definition.getCategory(), definition.getKey(), definition.getVersion(), definition.getResourceName(),
                definition.getDeploymentId(), definition.isSuspended(), definition.getHistoryTimeToLive(), definition.getTenantId());
    }

    public String jobInfo(Job job) {
        return String.format("(Id=%s, Duedate=%s, ProcessInstanceId=%s,%s, ProcessDefinitionId=%s, ExecutionId=%s, Retries=%d, ExceptionMessage=%s, FailedActivityId=%s, DeploymentId=%s, JobDefinitionId=%s, IsSuspended=%s, Priority=%d, CreateTime=%s, TenantId=%s)",
                job.getId(), job.getDuedate(), job.getProcessInstanceId(), job.getRootProcessInstanceId(), job.getProcessDefinitionId(),
                job.getExecutionId(), job.getRetries(), job.getExceptionMessage(), job.getFailedActivityId(), job.getDeploymentId(),
                job.getJobDefinitionId(), job.isSuspended(), job.getPriority(), job.getCreateTime(), job.getTenantId());
    }

    public String taskInfo(DelegateExecution execution) {
        String taskId = (String) execution.getVariable("task-id");
        return String.format("{%s, %s, %s, %s}", execution.getCurrentActivityName(), taskId, execution.getId(), execution.getProcessDefinitionId());
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
