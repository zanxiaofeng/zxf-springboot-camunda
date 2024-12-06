package zxf.camunda.arch.app.delegate;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zxf.camunda.arch.app.service.CamundaService;

@Slf4j
@Service
public class MyDelegate implements JavaDelegate {
    @Autowired
    private CamundaService camundaService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        execution.setVariable("paymentStatus", "OK");
    }
}
