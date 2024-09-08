package zxf.camunda.saga.task;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zxf.camunda.saga.service.CamundaService;

@Slf4j
@Component
public class App2Task2Adapter implements JavaDelegate {

    public App2Task2Adapter() {
        log.info("ctor()");
    }

    @Autowired
    private CamundaService camundaService;
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String taskId = (String) execution.getVariable("task-id");
        log.info("start, {}", camundaService.taskInfo(execution));

        orderServerB(execution, taskId);
        Thread.sleep(3000);

        log.info("end  , {}", camundaService.taskInfo(execution));
    }


    private void orderServerB(DelegateExecution execution, String taskId){
        execution.setVariable("VAR_OF_TASK2", "var of task2");

        if (taskId.endsWith("::2000")) {
            log.error("Failed to process task: {}", taskId);
            throw new RuntimeException("Failed to process task: " + taskId);
        }
    }
}