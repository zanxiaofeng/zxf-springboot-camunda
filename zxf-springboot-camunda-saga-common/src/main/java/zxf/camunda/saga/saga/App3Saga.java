package zxf.camunda.saga.saga;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.springframework.stereotype.Component;
import zxf.camunda.saga.base.SagaBuilder;
import zxf.camunda.saga.service.CamundaService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class App3Saga {
    private final String sagaName = "app3-v16";
    private final ProcessEngine processEngine;
    private final CamundaService camundaService;

    public void deploySaga() {
        log.info("{} deploySaga start", this.sagaName);

        try {
            if (camundaService.sagaRedeploy() || !isSagaDeployed()) {
                BpmnModelInstance bpmnModelInstance = buildSaga();
                Deployment deployment = processEngine.getRepositoryService().createDeployment()
                        .addModelInstance(this.sagaName + ".bpmn", bpmnModelInstance).deploy();
                log.info("{} saga deployment is done. (DeploymentId={})", this.sagaName, deployment.getId());
                return;
            }

            if (camundaService.registerDeployment()) {
                ProcessDefinition processDefinition = processEngine.getRepositoryService()
                        .createProcessDefinitionQuery().processDefinitionKey(this.sagaName).latestVersion().singleResult();
                processEngine.getManagementService()
                        .registerDeploymentForJobExecutor(processDefinition.getDeploymentId());
                log.info("{} saga had been deployed, register job executor to the latest deployment. (DeploymentId={})", this.sagaName, processDefinition.getDeploymentId());
            }
        } catch (Exception ex) {
            log.error("Exception when deploy {} saga or register job executor", this.sagaName, ex);
        }

        log.info("{} deploySaga end", this.sagaName);
    }

    public String trigger(Integer times, Integer count, Integer start) {
        start = Optional.ofNullable(start).orElse(3000);
        log.info("{} trigger start, {}, {}::{}~{}", this.sagaName, getPrefix(), times, start, count);
        for (int i = start; i < start + count; i++) {
            createInstance(times, i);
        }
        log.info("{} trigger end, {}, {}::{}~{}", this.sagaName, getPrefix(), times, start, count);
        return String.format("%s#%d-%d~%d", getPrefix(), times, start, count);
    }

    public void createInstance(Integer times, int number) {
        String taskId = getPrefix() + "#" + times + "-" + number;
        Map<String, Object> someVariables = new HashMap<>();
        someVariables.put("task-id", taskId);
        //This method will always create instance base on the latest version.
        //If you use business key, that business key must be unique.
        ProcessInstance processInstance = processEngine.getRuntimeService().startProcessInstanceByKey(this.sagaName, someVariables);
        log.info("{} instance, {}, {}", this.sagaName, taskId, camundaService.instanceInfo(processInstance));
    }

    public String getPrefix() {
        return "app3@" + camundaService.appName();
    }

    private Boolean isSagaDeployed() {
        Boolean hadBeenDeployed = processEngine.getRepositoryService().createProcessDefinitionQuery()
                .processDefinitionKey(this.sagaName).count() > 0;
        log.info("{} isSagaDeployed, {}", this.sagaName, hadBeenDeployed);
        return hadBeenDeployed;
    }

    private BpmnModelInstance buildSaga() {
        SagaBuilder sagaBuilder = SagaBuilder.newSaga(this.sagaName, camundaService.asyncBefore(), camundaService.asyncAfter())
                //R3/PT0S: execute at most 3 times, no delay between executions
                .activity("App3-Task 1", "zxf.camunda.saga.task.app3.App3Task1Adapter", "R3/PT0S")
                //R1/PT0S: execute at most 1 time, no retry
                .activityNoRetry("App3-Task 2", "zxf.camunda.saga.task.app3.App3Task2Adapter")
                //R3/PT5S: execute at most 3 times, 5 seconds between executions
                .activity("App3-Task 3", "zxf.camunda.saga.task.app3.App3Task3Adapter", "R3/PT5S")
                .end();
        return sagaBuilder.getModel();
    }
}
