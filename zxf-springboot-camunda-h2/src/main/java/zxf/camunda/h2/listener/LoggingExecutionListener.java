package zxf.camunda.h2.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;
import zxf.camunda.h2.service.CamundaService;

@Slf4j
@RequiredArgsConstructor
@Component
public class LoggingExecutionListener implements ExecutionListener {
    private final CamundaService camundaService;

    @Override
    public void notify(DelegateExecution execution) throws Exception {
        log.info("{}", camundaService.executionInfo(execution));
    }
}
