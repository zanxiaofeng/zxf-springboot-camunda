package zxf.camunda.h2.delegate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zxf.camunda.h2.service.CamundaService;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class Step1Delegate implements JavaDelegate {
    @Autowired
    private CamundaService camundaService;

    @Override
    public void execute(DelegateExecution execution) throws JsonProcessingException {
        execution.setVariable("step1Request", parseRequest(execution));
        execution.setVariable("step1Response", generateResponse());
        log.info("{}", camundaService.executionInfo(execution));
    }

    private HashMap parseRequest(DelegateExecution execution) throws JsonProcessingException {
        return new ObjectMapper().readValue((String) execution.getVariable("step1Request"), HashMap.class);
    }

    private Map generateResponse() {
        return Collections.singletonMap("value", "abc");
    }
}