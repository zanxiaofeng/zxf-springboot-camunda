package zxf.camunda.saga.task;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zxf.camunda.saga.service.CamundaService;

@Slf4j
@Component
public class CommonTask3Adapter implements JavaDelegate {

    public CommonTask3Adapter() {
        log.info("ctor()");
    }

    @Autowired
    private CamundaService camundaService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String taskId = (String) execution.getVariable("task-id");
        boolean isFirstExecution = camundaService.isFirstExecution(execution);
        boolean isLastExecution = camundaService.isLastExecution(execution);
        log.info("start, {}, isFirstExecution={}, isLastExecution={}", camundaService.taskInfo(execution), isFirstExecution, isLastExecution);

        if (taskId.endsWith("::3")) {
            log.error("Failed to process task: {}", taskId);
            throw new RuntimeException("Failed to process task: " + taskId);
            //After this, all camunda database change in this method  will be rollback(VARS...).
        }

        Thread.sleep(5000);

        log.info("end  , {}", camundaService.taskInfo(execution));
    }
}