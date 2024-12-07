package zxf.camunda.h2.delegate;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zxf.camunda.h2.service.CamundaService;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class LoanRequestDelegate implements JavaDelegate {
    @Autowired
    private CamundaService camundaService;

    @Override
    public void execute(DelegateExecution execution) {
        execution.setVariable("loanRequestResponse", generateResponse());
        log.info("{}", camundaService.executionInfo(execution));
    }

    private Map<String, Object> generateResponse() {
        return Collections.singletonMap("caseId", UUID.randomUUID().toString());
    }
}