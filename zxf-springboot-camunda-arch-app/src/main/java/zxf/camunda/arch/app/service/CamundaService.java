package zxf.camunda.arch.app.service;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.Execution;
import org.camunda.bpm.engine.runtime.Job;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.runtime.ProcessInstanceWithVariables;
import org.camunda.bpm.engine.variable.VariableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zxf.camunda.arch.app.exception.BusinessErrorException;
import zxf.camunda.arch.app.exception.BusinessErrors;

import java.util.Collections;
import java.util.Map;

@Slf4j
@Component
public class CamundaService {
    @Autowired
    private ProcessEngine processEngine;

    public String instanceInfo(ProcessInstance instance) {
        return String.format("(ProcessInstanceId=%s, %s, ProcessDefinitionId=%s, BusinessKey=%s, CaseInstanceId=%s, isSuspended=%s, TenantId=%s)", instance.getProcessInstanceId(), instance.getRootProcessInstanceId(), instance.getProcessDefinitionId(), instance.getBusinessKey(), instance.getCaseInstanceId(), instance.isSuspended(), instance.getTenantId());
    }

    public String definitionInfo(ProcessDefinition definition) {
        return String.format("(Id=%s, Category=%s, Key=%s, Version=%s, ResourceName=%s, DeploymentId=%s, isSuspended=%s, HistoryTimeToLive=%d, TenantId=%s)", definition.getId(), definition.getCategory(), definition.getKey(), definition.getVersion(), definition.getResourceName(), definition.getDeploymentId(), definition.isSuspended(), definition.getHistoryTimeToLive(), definition.getTenantId());
    }

    public String jobInfo(Job job) {
        return String.format("(Id=%s, Duedate=%s, ProcessInstanceId=%s,%s, ProcessDefinitionId=%s, ExecutionId=%s, Retries=%d, ExceptionMessage=%s, FailedActivityId=%s, DeploymentId=%s, JobDefinitionId=%s, IsSuspended=%s, Priority=%d, CreateTime=%s, TenantId=%s)", job.getId(), job.getDuedate(), job.getProcessInstanceId(), job.getRootProcessInstanceId(), job.getProcessDefinitionId(), job.getExecutionId(), job.getRetries(), job.getExceptionMessage(), job.getFailedActivityId(), job.getDeploymentId(), job.getJobDefinitionId(), job.isSuspended(), job.getPriority(), job.getCreateTime(), job.getTenantId());
    }

    public String executionInfo(Execution execution) {
        return String.format("(Id=%s, ProcessInstanceId=%s)", execution.getId(), execution.getProcessInstanceId());
    }

    public String executionInfo(DelegateExecution execution, Boolean shortenFormat) {
        //Note: The DelegateExecution::getEventName() is only for ExecutionListener.
        return String.format("(Id=%s::%s, BusinessKey=%s, CurrentActivityName=%s..%s, ProcessDefinitionId=%s, ProcessInstanceId=%s, ProcessBusinessKey=%s, ActivityInstance=%s::%s, VariableScopeKey=%s, Variables=%s, Locals=%s)",
                execution.getId(), execution.getParentId(), execution.getBusinessKey(),
                execution.getCurrentActivityName(), execution.getEventName(),
                execution.getProcessDefinitionId(), execution.getProcessInstanceId(), execution.getProcessBusinessKey(),
                execution.getActivityInstanceId(), execution.getParentActivityInstanceId(),
                execution.getVariableScopeKey(), shortenFormat ? execution.getVariableNames() : execution.getVariables(), execution.getVariableNamesLocal());
    }

    public void correlateMessage(String messageName, String businessKey, Map<String, Object> processVariables) throws BusinessErrorException {
        try {
            processEngine.getRuntimeService().correlateMessage(messageName, businessKey, processVariables);
        } catch (Exception ex) {
            throw new BusinessErrorException(BusinessErrors.APP_FLOW_002.getCode(), BusinessErrors.APP_FLOW_002.getDescription() + messageName + ", " + businessKey);
        }
    }

    public VariableMap startWithVariablesInReturn(String processDefinitionKey, String businessKey, Map<String, Object> processVariables) throws BusinessErrorException {
        try {
            ProcessInstanceWithVariables processInstance = processEngine.getRuntimeService()
                    .createProcessInstanceByKey(processDefinitionKey)
                    .businessKey(businessKey).setVariables(processVariables).executeWithVariablesInReturn();
            VariableMap returnVariables = processInstance.getVariables();
            returnVariables.put("ProcessInstanceId", processInstance.getProcessInstanceId());
            return returnVariables;
        } catch (Exception ex) {
            throw new BusinessErrorException(BusinessErrors.APP_FLOW_001.getCode(), BusinessErrors.APP_FLOW_001.getDescription() + processDefinitionKey + ", " + businessKey);
        }
    }
}
