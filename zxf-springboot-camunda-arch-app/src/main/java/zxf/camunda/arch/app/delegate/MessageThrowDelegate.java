package zxf.camunda.arch.app.delegate;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.model.bpmn.instance.MessageEventDefinition;
import org.camunda.bpm.model.bpmn.instance.ThrowEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zxf.camunda.arch.app.service.CamundaService;

@Slf4j
@Service
public class MessageThrowDelegate implements JavaDelegate {
    @Autowired
    private CamundaService camundaService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        ThrowEvent messageEvent = (ThrowEvent) execution.getBpmnModelElementInstance();
        MessageEventDefinition messageEventDefinition = (MessageEventDefinition) messageEvent.getEventDefinitions().iterator().next();
        String throwMessageName = messageEventDefinition.getMessage().getName();
        execution.getProcessEngineServices().getRuntimeService().correlateMessage(throwMessageName, execution.getBusinessKey());
        log.info("Throw message '{}' to businessKey {} from activity '{}'", throwMessageName, execution.getBusinessKey(), execution.getCurrentActivityName());
    }
}
