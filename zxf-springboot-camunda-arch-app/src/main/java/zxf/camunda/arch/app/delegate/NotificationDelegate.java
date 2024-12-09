package zxf.camunda.arch.app.delegate;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NotificationDelegate implements JavaDelegate {
    @Autowired
    ProcessEngine processEngine;

    @Override
    public void execute(DelegateExecution execution) throws JsonProcessingException {
        String message = (String) execution.getVariable("message");
        log.info("notification.start, {}", message);

        processEngine.getRuntimeService()
                .createMessageCorrelation("PaymentProcess.Notification")
                .processInstanceBusinessKey(execution.getBusinessKey())
                .correlateWithResult();
        log.info("notification.end, {}", message);
    }
}