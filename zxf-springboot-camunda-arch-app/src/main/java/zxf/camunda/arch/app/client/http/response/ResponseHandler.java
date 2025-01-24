package zxf.camunda.arch.app.client.http.response;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.springframework.http.ResponseEntity;
import zxf.camunda.arch.app.exception.BusinessErrorException;
import zxf.camunda.arch.app.exception.DownstreamErrorException;

import java.util.Map;

public interface ResponseHandler {
    void handle(DelegateExecution execution, ResponseEntity<Map<String, Object>> response, Map<String, String> handleSetting) throws BusinessErrorException;
}
