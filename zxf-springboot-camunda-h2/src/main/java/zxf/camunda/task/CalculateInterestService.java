package zxf.camunda.task;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CalculateInterestService implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) {
        log.info("calculating interest of the loan");
    }
}