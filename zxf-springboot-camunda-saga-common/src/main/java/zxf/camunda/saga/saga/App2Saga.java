package zxf.camunda.saga.saga;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zxf.camunda.saga.base.SagaBuilder;
import zxf.camunda.saga.service.CamundaService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
public class App2Saga {
    private final String sagaName = "app2-v16";
    @Autowired
    private ProcessEngine processEngine;
    @Autowired
    private CamundaService camundaService;

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

        log.info("{} deploySaga start", this.sagaName);
    }

    public String trigger(Integer times, Integer count, Integer start) {
        start = Optional.ofNullable(start).orElse(2000);
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
        return "app2@" + camundaService.appName();
    }

    private Boolean isSagaDeployed() {
        Boolean hadBeenDeployed = processEngine.getRepositoryService().createProcessDefinitionQuery()
                .processDefinitionKey(this.sagaName).count() > 0;
        log.info("{} isSagaDeployed, {}", this.sagaName, hadBeenDeployed);
        return hadBeenDeployed;
    }

    private BpmnModelInstance buildSaga() {
        SagaBuilder sagaBuilder = SagaBuilder.newSaga(this.sagaName, camundaService.asyncBefore(), camundaService.asyncAfter())
                .activityNoRetry("App2-Task 1", "zxf.camunda.saga.task.app2.App2Task1Adapter")
                .compensationActivity("App2-Undo Task 1", "zxf.camunda.saga.task.app2.App2Task1UndoAdapter")
                .activityNoRetry("App2-Task 2", "zxf.camunda.saga.task.app2.App2Task2Adapter")
                .compensationActivity("App2-Undo Task 2", "zxf.camunda.saga.task.app2.App2Task2UndoAdapter")
                .activityNoRetry("App2-Task 3", "zxf.camunda.saga.task.app2.App2Task3Adapter")
                .end()
                .triggerCompensationActivityOnAnyError("App2-Finally Undo", "zxf.camunda.saga.task.app2.App2TaskEndUndoAdapter");
        //Undo flow: Undo Task 2 --> Undo Task 1 --> Finally Undo
        return sagaBuilder.getModel();
    }
}
