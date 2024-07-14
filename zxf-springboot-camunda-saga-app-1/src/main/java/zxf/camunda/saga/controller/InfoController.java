package zxf.camunda.saga.controller;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/info")
public class InfoController {
    @Autowired
    private ProcessEngine processEngine;

    @GetMapping("/definitions")
    public List<String> definitions() {
        log.info("definitions");
        List<ProcessDefinition> definitions = processEngine.getRepositoryService().createProcessDefinitionQuery().list();
        return definitions.stream().map(this::definitionInfo).collect(Collectors.toList());
    }

    @GetMapping("/instances")
    public List<String> instances() {
        log.info("instances");
        List<ProcessInstance> instances = processEngine.getRuntimeService().createProcessInstanceQuery().list();
        return instances.stream().map(this::instanceInfo).collect(Collectors.toList());
    }

    @GetMapping("/deployments/registered")
    public List<String> registeredDeployments() {
        log.info("registeredDeployments");
        Set<String> registeredDeployments = processEngine.getManagementService().getRegisteredDeployments();
        return registeredDeployments.stream().map((deploymentId) -> processEngine.getRepositoryService().createProcessDefinitionQuery().deploymentId(deploymentId).singleResult()).map(this::definitionInfo).collect(Collectors.toList());
    }

    private String instanceInfo(ProcessInstance instance) {
        ProcessDefinition definition = processEngine.getRepositoryService().createProcessDefinitionQuery()
                .processDefinitionId(instance.getProcessDefinitionId()).singleResult();
        return String.format("(Id=%s, definition=%s)", instance.getId(), definitionInfo(definition));
    }

    private String definitionInfo(ProcessDefinition definition) {
        return String.format("(Id=%s, Version=%s, DeploymentId=%s, isSuspended=%s)",
                definition.getId(), definition.getVersion(),
                definition.getDeploymentId(), definition.isSuspended());
    }
}
