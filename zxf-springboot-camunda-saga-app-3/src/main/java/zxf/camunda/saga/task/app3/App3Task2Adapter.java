package zxf.camunda.saga.task.app3;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zxf.camunda.saga.service.CamundaService;

@Slf4j
@Component
public class App3Task2Adapter implements JavaDelegate {

    public App3Task2Adapter() {
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

        if (taskId.endsWith("::2000")) {
            log.error("Failed to process task: {}", taskId);
            throw new RuntimeException("Failed to process task: " + taskId);
            //After this, all camunda database change in this method  will be rollback(VARS...).
        }

        Thread.sleep(3000);

        log.info("end  , {}", camundaService.taskInfo(execution));
    }
}