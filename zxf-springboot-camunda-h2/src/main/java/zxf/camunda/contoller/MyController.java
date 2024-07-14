package zxf.camunda.contoller;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RestController
public class MyController {
    @Autowired
    ProcessEngine processEngine;

    @GetMapping("/process/{processKey}/definitions")
    public List<String> processDefinitions(@PathVariable String processKey) {
        log.info("MyController::processDefinitions, " + processKey);
        List<ProcessDefinition> definitions = processEngine.getRepositoryService().createProcessDefinitionQuery().processDefinitionKey(processKey).list();
        return definitions.stream().map(ProcessDefinition::toString).collect(Collectors.toList());
    }

    @GetMapping("/process/{processKey}/start")
    public String startProcess(@PathVariable String processKey) {
        log.info("MyController::startProcess, " + processKey);
        ProcessInstance processInstance = processEngine.getRuntimeService().startProcessInstanceByKey(processKey);
        return processInstance.getProcessInstanceId();
    }

    @GetMapping("/process/{processKey}/instances")
    public List<String> processInstances(@PathVariable String processKey) {
        log.info("MyController::processInstances, " + processKey);
        List<ProcessInstance> instances = processEngine.getRuntimeService().createProcessInstanceQuery().processDefinitionKey(processKey).list();
        return instances.stream().map(ProcessInstance::getProcessInstanceId).collect(Collectors.toList());
    }

    @GetMapping("/process/{processKey}/tasks")
    public List<String> processTasks(@PathVariable String processKey) {
        log.info("MyController::processTasks, " + processKey);
        List<Task> tasks = processEngine.getTaskService().createTaskQuery().processDefinitionKey(processKey).list();
        return tasks.stream().map(Task::toString).collect(Collectors.toList());
    }

    @GetMapping("/process/{processKey}/tasks/{taskId}/complete")
    public void completeProcessTask(@PathVariable String processKey, @PathVariable String taskId) {
        log.info("MyController::completeProcessTask, " + processKey, ", " + taskId);
        processEngine.getTaskService().complete(taskId);
        log.info("MyController::completeProcessTask, task: {}", taskId);
    }

    @GetMapping("/deployments/registered")
    public List<String> registeredDeployments() {
        log.info("MyController::registeredDeployments");
        log.info("registeredDeployments");
        Set<String> registeredDeployments = processEngine.getManagementService().getRegisteredDeployments();
        return registeredDeployments.stream().map((deploymentId) -> processEngine.getRepositoryService().createProcessDefinitionQuery().deploymentId(deploymentId).singleResult()).map(this::definitionInfo).collect(Collectors.toList());
    }

    private String definitionInfo(ProcessDefinition definition) {
        return String.format("(Id=%s, Version=%s, DeploymentId=%s, isSuspended=%s)",
                definition.getId(), definition.getVersion(),
                definition.getDeploymentId(), definition.isSuspended());
    }
}
