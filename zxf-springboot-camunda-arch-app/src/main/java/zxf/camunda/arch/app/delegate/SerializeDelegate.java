package zxf.camunda.arch.app.delegate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zxf.camunda.arch.app.service.CamundaService;

@Slf4j
@Service
public class SerializeDelegate implements JavaDelegate {
    @Autowired
    private CamundaService camundaService;

    @Override
    public void execute(DelegateExecution execution) throws JsonProcessingException {
        String variableIn = (String) execution.getVariable("variableIn");
        String variableOut = (String) execution.getVariable("variableOut");
        execution.setVariable(variableOut, serializeVariable(variableIn, execution));
        log.info("Serialize, {}", camundaService.executionInfoForService(execution, false));
    }

    protected String serializeVariable(String variableName, DelegateExecution execution) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(execution.getVariable(variableName));
    }
}