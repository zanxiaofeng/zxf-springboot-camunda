package zxf.camunda.saga.task;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import zxf.camunda.saga.service.CamundaService;

@Slf4j
public class CommonTask1Adapter implements JavaDelegate {

    public CommonTask1Adapter() {
        log.info("ctor()");
    }

    @Autowired
    private CamundaService camundaService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String taskId = (String) execution.getVariable("task-id");
        boolean isFirstExecution = camundaService.isFirstExecution(execution);
        log.info("start, {}, {}, isFirstExecution={}", taskId, execution.getId(), isFirstExecution);

        if (taskId.endsWith("::1")) {
            log.error("Failed to process task: {}", taskId);
            throw new RuntimeException("Failed to process task: " + taskId);
            //After this, all camunda database change in this method  will be rollback(VARS...).
        }

        Thread.sleep(5000);

        log.info("end, {}, {}", taskId, execution.getId());
    }
}