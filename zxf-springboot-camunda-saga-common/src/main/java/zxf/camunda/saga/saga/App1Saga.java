package zxf.camunda.saga.saga;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import zxf.camunda.saga.base.SagaBuilder;
import zxf.camunda.saga.service.CamundaService;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class App1Saga {
    private final String sagaName = "app1-v10";
    @Autowired
    private ProcessEngine processEngine;
    @Autowired
    private CamundaService camundaService;
    @Value("${saga.re-deploy}")
    private boolean sagaRedeploy;
    @Value("${camunda.bpm.job-execution.deployment-aware}")
    private boolean deploymentAware;

    public void deploySaga() {
        log.info("{} deploySaga start", this.sagaName);

        try {
            if (sagaRedeploy || !isSagaDeployed()) {
                BpmnModelInstance bpmnModelInstance = buildSaga();
                Deployment deployment = processEngine.getRepositoryService().createDeployment()
                        .addModelInstance(this.sagaName + ".bpmn", bpmnModelInstance).deploy();
                log.info("{} saga deployment is done. (DeploymentId={})", this.sagaName, deployment.getId());
                return;
            }

            if (deploymentAware) {
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

    public void trigger(String prefix, Integer times, Integer count) {
        log.info("{} trigger start, {}, {}::{}", this.sagaName, prefix, times, count);
        for (int i = 0; i < count; i++) {
            String taskId = prefix + "#" + times + "-" + i;
            Map<String, Object> someVariables = new HashMap<>();
            someVariables.put("task-id", taskId);
            //This method will always create instance base on the latest version.
            ProcessInstance processInstance = processEngine.getRuntimeService().startProcessInstanceByKey(this.sagaName, taskId, someVariables);
            log.info("{} instance, {}", this.sagaName, camundaService.instanceInfo(processInstance));
        }
        log.info("{} trigger end, {}, {}::{}", this.sagaName, prefix, times, count);
    }

    private Boolean isSagaDeployed() {
        Boolean hadBeenDeployed = processEngine.getRepositoryService().createProcessDefinitionQuery()
                .processDefinitionKey(this.sagaName).count() > 0;
        log.info("{} isSagaDeployed, {}", this.sagaName, hadBeenDeployed);
        return hadBeenDeployed;
    }

    private BpmnModelInstance buildSaga() {
        SagaBuilder sagaBuilder = SagaBuilder.newSaga(this.sagaName, true)
                .activityNoRetry("Task 1", "zxf.camunda.saga.task.app1.App1Task1Adapter")
                .compensationActivity("Undo Task 1", "zxf.camunda.saga.task.app1.App1Task1UndoAdapter")
                .activityNoRetry("Task 2", "zxf.camunda.saga.task.app1.App1Task2Adapter")
                .compensationActivity("Undo Task 2", "zxf.camunda.saga.task.app1.App1Task2UndoAdapter")
                .activityNoRetry("Task 3", "zxf.camunda.saga.task.app1.App1Task3Adapter")
                .end()
                .triggerCompensationOnAnyError();
        return sagaBuilder.getModel();
    }
}
