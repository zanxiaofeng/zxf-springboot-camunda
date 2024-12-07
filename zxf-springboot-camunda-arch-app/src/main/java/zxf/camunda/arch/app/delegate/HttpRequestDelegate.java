package zxf.camunda.arch.app.delegate;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import zxf.camunda.arch.app.service.CamundaService;

@Slf4j
@Service
public class HttpRequestDelegate implements JavaDelegate {
    @Autowired
    private CamundaService camundaService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        execution.setVariable("paymentStatus", "OK");

        if (StringUtils.hasText((String) execution.getVariable("errorInfo"))) {
            String error = (String) execution.getVariable("errorInfo");
            execution.removeVariable("errorInfo");
            throw new BpmnError(error, error);
        }
    }
}
