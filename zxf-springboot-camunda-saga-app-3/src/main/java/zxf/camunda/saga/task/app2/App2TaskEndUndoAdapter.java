package zxf.camunda.saga.task.app2;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zxf.camunda.saga.service.CamundaService;

@Slf4j
@Component
public class App2TaskEndUndoAdapter implements JavaDelegate {

    public App2TaskEndUndoAdapter() {
        log.info("ctor()");
    }

    @Autowired
    private CamundaService camundaService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String taskId = (String) execution.getVariable("task-id");
        log.info("start, {}", camundaService.taskInfo(execution));
        log.info("threads, {}", camundaService.threadInfo(execution));

        String varOfTask1 = (String) execution.getVariable("VAR_OF_TASK1");
        String varOfTask2 = (String) execution.getVariable("VAR_OF_TASK2");
        String varOfTask3 = (String) execution.getVariable("VAR_OF_TASK3");
        log.info("vars, taskId={}, VAR_OF_TASK1={}, VAR_OF_TASK2={}, VAR_OF_TASK3={}", taskId, varOfTask1, varOfTask2, varOfTask3);

        log.info("end  , {}", camundaService.taskInfo(execution));
    }
}