package zxf.camunda.arch.app.contoller;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.Execution;
import org.camunda.bpm.engine.runtime.Job;
import org.camunda.bpm.engine.runtime.JobQuery;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import zxf.camunda.arch.app.service.CamundaService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/info")
public class InfoController {
    @Autowired
    private ProcessEngine processEngine;
    @Autowired
    private CamundaService camundaService;

    @GetMapping("/definitions")
    public List<String> definitions() {
        log.info("definitions");
        List<ProcessDefinition> definitions = processEngine.getRepositoryService().createProcessDefinitionQuery().list();
        return definitions.stream().map(camundaService::definitionInfo).collect(Collectors.toList());
    }

    @GetMapping("/definitions/message")
    public List<String> definitionsByMessage(@RequestParam String message) {
        log.info("definitionsByMessage");
        List<ProcessDefinition> definitions = processEngine.getRepositoryService().createProcessDefinitionQuery().messageEventSubscriptionName(message).list();
        return definitions.stream().map(camundaService::definitionInfo).collect(Collectors.toList());
    }

    @GetMapping("/instances")
    public List<String> instances() {
        log.info("instances");
        List<ProcessInstance> instances = processEngine.getRuntimeService().createProcessInstanceQuery().list();
        return instances.stream().map(camundaService::instanceInfo).collect(Collectors.toList());
    }

    @GetMapping("/executions")
    public List<String> executions() {
        log.info("executions");
        List<Execution> instances = processEngine.getRuntimeService().createExecutionQuery().list();
        return instances.stream().map(camundaService::executionInfo).collect(Collectors.toList());
    }

    @GetMapping("/executions/message")
    public List<String> executionsByMessage(@RequestParam String message) {
        log.info("executionsByMessage");
        List<Execution> instances = processEngine.getRuntimeService().createExecutionQuery().messageEventSubscriptionName(message).list();
        return instances.stream().map(camundaService::executionInfo).collect(Collectors.toList());
    }

    @GetMapping("/tasks/all")
    public List<String> allTasks() {
        log.info("allTasks");
        List<Task> tasks = processEngine.getTaskService().createTaskQuery().processDefinitionKey("PaymentProcess").list();
        return tasks.stream().map(Task::toString).collect(Collectors.toList());
    }

    @GetMapping("/jobs/all")
    public List<String> allJobs() {
        log.info("allJobs");
        JobQuery jobQuery = processEngine.getManagementService().createJobQuery().processDefinitionKey("PaymentProcess");
        List<Job> allJobs = jobQuery.list();
        return allJobs.stream().map(camundaService::jobInfo).collect(Collectors.toList());
    }

    @GetMapping("/jobs/failed")
    public List<String> failedJobs() {
        log.info("failedJobs");
        JobQuery jobQuery = processEngine.getManagementService().createJobQuery().processDefinitionKey("PaymentProcess");
        List<Job> failedJobs = jobQuery.withException().list();
        return failedJobs.stream().map(camundaService::jobInfo).collect(Collectors.toList());
    }

    @GetMapping("/jobs/active")
    public List<String> activeJobs() {
        log.info("activeJobs");
        JobQuery jobQuery = processEngine.getManagementService().createJobQuery().processDefinitionKey("PaymentProcess");
        List<Job> failedJobs = jobQuery.active().orderByJobRetries().desc().list();
        return failedJobs.stream().map(camundaService::jobInfo).collect(Collectors.toList());
    }

    @GetMapping("/jobs/retry")
    public List<String> retryJobs() {
        log.info("retryJobs");
        JobQuery jobQuery = processEngine.getManagementService().createJobQuery().processDefinitionKey("PaymentProcess");
        List<Job> failedJobs = jobQuery.withRetriesLeft().orderByJobRetries().desc().list();
        return failedJobs.stream().map(camundaService::jobInfo).collect(Collectors.toList());
    }

    @GetMapping("/deployments/registered")
    public List<String> registeredDeployments() {
        log.info("registeredDeployments");
        Set<String> registeredDeployments = processEngine.getManagementService().getRegisteredDeployments();
        return registeredDeployments.stream().map((deploymentId) -> processEngine.getRepositoryService()
                        .createProcessDefinitionQuery().deploymentId(deploymentId).singleResult())
                .map(camundaService::definitionInfo).collect(Collectors.toList());
    }
}
