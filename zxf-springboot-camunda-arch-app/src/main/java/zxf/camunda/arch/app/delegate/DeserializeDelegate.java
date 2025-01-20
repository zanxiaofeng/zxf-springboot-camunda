package zxf.camunda.arch.app.delegate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zxf.camunda.arch.app.service.CamundaService;

import java.lang.reflect.Type;
import java.util.HashMap;

@Slf4j
@Service
public class DeserializeDelegate implements JavaDelegate {
    @Autowired
    private CamundaService camundaService;

    @Override
    public void execute(DelegateExecution execution) throws JsonProcessingException {
        String variableIn = (String) execution.getVariable("variableIn");
        String variableOut = (String) execution.getVariable("variableOut");
        execution.setVariable(variableOut, deserializeVariable(variableIn, execution));
        log.info("Deserialize, {}", camundaService.executionInfoForService(execution, false));
    }

    protected HashMap<String, Object> deserializeVariable(String variableName, DelegateExecution execution) throws JsonProcessingException {
        return new ObjectMapper().readValue((String) execution.getVariable(variableName), new TypeReference<HashMap<String, Object>>() {
            @Override
            public Type getType() {
                return super.getType();
            }
        });
    }
}