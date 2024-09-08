package zxf.camunda.saga.controller;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.Job;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zxf.camunda.saga.service.CamundaService;

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

    @GetMapping("/instances")
    public List<String> instances() {
        log.info("instances");
        List<ProcessInstance> instances = processEngine.getRuntimeService().createProcessInstanceQuery().list();
        return instances.stream().map(camundaService::instanceInfo).collect(Collectors.toList());
    }

    @GetMapping("/jobs/all")
    public List<String> allJobs() {
        log.info("failedJobs");
        List<Job> allJobs = processEngine.getManagementService().createJobQuery().list();
        return allJobs.stream().map(camundaService::jobInfo).collect(Collectors.toList());
    }

    @GetMapping("/jobs/active")
    public List<String> activeJobs() {
        log.info("failedJobs");
        List<Job> failedJobs = processEngine.getManagementService().createJobQuery()
                .active().orderByJobRetries().desc().list();
        return failedJobs.stream().map(camundaService::jobInfo).collect(Collectors.toList());
    }

    @GetMapping("/jobs/retry")
    public List<String> retryJobs() {
        log.info("failedJobs");
        List<Job> failedJobs = processEngine.getManagementService().createJobQuery()
                .withRetriesLeft().orderByJobRetries().desc().list();
        return failedJobs.stream().map(camundaService::jobInfo).collect(Collectors.toList());
    }

    @GetMapping("/jobs/failed")
    public List<String> failedJobs() {
        log.info("failedJobs");
        List<Job> failedJobs = processEngine.getManagementService().createJobQuery().withException().list();
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
