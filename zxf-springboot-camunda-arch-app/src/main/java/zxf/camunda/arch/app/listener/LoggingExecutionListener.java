package zxf.camunda.arch.app.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;
import zxf.camunda.arch.app.service.CamundaService;

@Slf4j
@RequiredArgsConstructor
@Component
public class LoggingExecutionListener implements ExecutionListener {
    private static Boolean SHORTEN_FORMAT = true;
    private final CamundaService camundaService;

    @Override
    public void notify(DelegateExecution execution) throws Exception {
        log.info("{}", camundaService.executionInfoForListener(execution, SHORTEN_FORMAT));
    }
}
