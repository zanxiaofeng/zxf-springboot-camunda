package zxf.camunda.arch.app.delegate;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import zxf.camunda.arch.app.client.HttpClient;
import zxf.camunda.arch.app.service.CamundaService;

import java.util.Map;

@Slf4j
@Service
public class HttpRequestDelegate implements JavaDelegate {
    @Autowired
    private CamundaService camundaService;
    @Autowired
    private HttpClient httpClient;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        try {
            String method = (String) execution.getVariable("method");
            String url = (String) execution.getVariable("url");
            String body = (String) execution.getVariable("body");
            Map<String, String> headers = (Map<String, String>) execution.getVariable("headers");
            String responseVariable = (String) execution.getVariable("responseVariable");
            execution.setVariable(responseVariable, httpClient.request(method, url, body, headers));
        } catch (Exception ex) {
            log.error("Exception when sending http request", ex);
        } finally {
            execution.removeVariable("method");
            execution.removeVariable("url");
            execution.removeVariable("body");
            execution.removeVariable("headers");
        }
    }

    private void error(DelegateExecution execution) {
        execution.setVariable("paymentStatus", "OK");

        if (StringUtils.hasText((String) execution.getVariable("errorInfo"))) {
            String error = (String) execution.getVariable("errorInfo");
            execution.removeVariable("errorInfo");
            throw new BpmnError(error, error);
        }
    }
}
