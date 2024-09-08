package zxf.camunda.saga.task;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zxf.camunda.saga.service.CamundaService;

@Slf4j
@Component
public class CommonTask1Adapter implements JavaDelegate {

    public CommonTask1Adapter() {
        log.info("ctor()");
    }

    @Autowired
    private CamundaService camundaService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        boolean isFirstExecution = camundaService.isFirstExecution(execution);
        boolean isLastExecution = camundaService.isLastExecution(execution);
        log.info("start, {}, isFirstExecution={}, isLastExecution={}", camundaService.taskInfo(execution), isFirstExecution, isLastExecution);

        Thread.sleep(1000);

        log.info("end  , {}", camundaService.taskInfo(execution));
    }
}