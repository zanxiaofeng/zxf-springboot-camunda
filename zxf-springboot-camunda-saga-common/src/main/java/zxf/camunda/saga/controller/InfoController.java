package zxf.camunda.saga.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.externaltask.ExternalTask;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.Job;
import org.camunda.bpm.engine.runtime.JobQuery;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import zxf.camunda.saga.service.CamundaService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/info")
public class InfoController {
    private final ProcessEngine processEngine;
    private final CamundaService camundaService;

    @GetMapping("/definitions")
    public List<String> definitions() {
        log.info("definitions");
        List<ProcessDefinition> definitions = processEngine.getRepositoryService().createProcessDefinitionQuery().list();
        return definitions.stream().map(camundaService::definitionInfo).collect(Collectors.toList());
    }

    @GetMapping("/instances")
    public List<String> instances() {
        log.info("instances");
        List<ProcessInstance> instances = processEngine.getRuntimeService().createProcessInstanceQuery().list();
        return instances.stream().map(camundaService::instanceInfo).collect(Collectors.toList());
    }

    @GetMapping("/jobs/all")
    public List<String> allJobs(@RequestParam(required = false) String processDefinitionId) {
        log.info("failedJobs");
        JobQuery jobQuery = processEngine.getManagementService().createJobQuery();
        if (processDefinitionId != null) {
            jobQuery = jobQuery.processDefinitionId(processDefinitionId);
        }
        List<Job> allJobs = jobQuery.list();
        return allJobs.stream().map(camundaService::jobInfo).collect(Collectors.toList());
    }

    @GetMapping("/jobs/failed")
    public List<String> failedJobs(@RequestParam(required = false) String processDefinitionId) {
        log.info("failedJobs");
        JobQuery jobQuery = processEngine.getManagementService().createJobQuery();
        if (processDefinitionId != null) {
            jobQuery = jobQuery.processDefinitionId(processDefinitionId);
        }
        List<Job> failedJobs = jobQuery.withException().list();
        return failedJobs.stream().map(camundaService::jobInfo).collect(Collectors.toList());
    }

    @GetMapping("/jobs/active")
    public List<String> activeJobs(@RequestParam(required = false) String processDefinitionId) {
        log.info("activeJobs");
        JobQuery jobQuery = processEngine.getManagementService().createJobQuery();
        if (processDefinitionId != null) {
            jobQuery = jobQuery.processDefinitionId(processDefinitionId);
        }
        List<Job> activeJobs = jobQuery.active().orderByJobRetries().desc().list();
        return activeJobs.stream().map(camundaService::jobInfo).collect(Collectors.toList());
    }

    @GetMapping("/jobs/retry")
    public List<String> retryJobs(@RequestParam(required = false) String processDefinitionId) {
        log.info("retryJobs");
        JobQuery jobQuery = processEngine.getManagementService().createJobQuery();
        if (processDefinitionId != null) {
            jobQuery = jobQuery.processDefinitionId(processDefinitionId);
        }
        List<Job> retryableJobs = jobQuery.withRetriesLeft().orderByJobRetries().desc().list();
        return retryableJobs.stream().map(camundaService::jobInfo).collect(Collectors.toList());
    }


    @GetMapping("/deployments/registered")
    public List<String> registeredDeployments() {
        log.info("registeredDeployments");
        Set<String> registeredDeployments = processEngine.getManagementService().getRegisteredDeployments();
        return registeredDeployments.stream().map((deploymentId) -> processEngine.getRepositoryService()
                        .createProcessDefinitionQuery().deploymentId(deploymentId).singleResult())
                .map(camundaService::definitionInfo).collect(Collectors.toList());
    }

    @GetMapping("/external-tasks")
    public List<String> externalTasks() {
        log.info("externalTasks");
        List<ExternalTask> tasks = processEngine.getExternalTaskService().createExternalTaskQuery().list();
        return tasks.stream().map(t -> String.format("(Id=%s, Topic=%s, ProcessInstanceId=%s, Retries=%d, WorkerId=%s, LockExpirationTime=%s)",
                t.getId(), t.getTopicName(), t.getProcessInstanceId(), t.getRetries(), t.getWorkerId(), t.getLockExpirationTime()))
                .collect(Collectors.toList());
    }
}
