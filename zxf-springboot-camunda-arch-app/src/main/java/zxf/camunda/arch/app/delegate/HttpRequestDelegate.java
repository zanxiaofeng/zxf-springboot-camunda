package zxf.camunda.arch.app.delegate;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import zxf.camunda.arch.app.client.HttpClient;
import zxf.camunda.arch.app.client.http.response.ResponseHandler;
import zxf.camunda.arch.app.exception.BusinessErrorException;
import zxf.camunda.arch.app.exception.DownstreamErrorException;
import zxf.camunda.arch.app.exception.BusinessErrors;
import zxf.camunda.arch.app.service.CamundaService;

import java.util.Map;

@Slf4j
@Service
public class HttpRequestDelegate implements JavaDelegate {
    @Autowired
    private CamundaService camundaService;
    @Autowired
    private HttpClient httpClient;
    @Autowired
    private BeanFactory beanFactory;

    /* Note:
    1. Do not need to remove local variables.
    2. new BpmnError(error, error) will not cause the retry.
    3. In bpmn fle, the input variables are local variables.
    4. In bpmn fle, the output variables are process variables.
    */
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        try {
            String method = (String) execution.getVariable("method");
            String url = (String) execution.getVariable("url");
            Map<String, String> headers = (Map<String, String>) execution.getVariable("headers");
            String body = (String) execution.getVariable("body");
            ResponseEntity<Map<String, Object>> response = httpClient.request(method, url, body, headers);

            String responseHandlerBeanName = (String) execution.getVariable("responseHandler");
            Map<String, String> responseHandleSetting = (Map<String, String>) execution.getVariable("responseHandleSetting");
            ResponseHandler responseHandler = beanFactory.getBean(responseHandlerBeanName, ResponseHandler.class);

            if (responseHandleSetting != null) {
                responseHandler.handle(execution, response, responseHandleSetting);
            }
        } catch (BusinessErrorException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error("Exception when sending http request", ex);
            throw DownstreamErrorException.downstreamErrorWithException(ex);
        }
    }
}
