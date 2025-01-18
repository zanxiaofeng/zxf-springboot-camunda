package zxf.camunda.arch.app.contoller.app;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.runtime.ProcessInstanceWithVariables;
import org.camunda.bpm.engine.variable.VariableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/app/payment")
public class PaymentController {
    @Autowired
    ProcessEngine processEngine;

    @GetMapping("/normal-start")
    public VariableMap normalStart(@RequestParam String orderId, @RequestParam String paymentOrderCode, @RequestParam String shippingRequestCode, @RequestParam String shippingOrderCode) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("OrderId", orderId);
        variables.put("paymentOrderCode", paymentOrderCode);
        variables.put("shippingRequestCode", shippingRequestCode);
        variables.put("shippingOrderCode", shippingOrderCode);

        log.info("normalStart, {}, {}", orderId, variables);
        ProcessInstanceWithVariables processInstance = processEngine.getRuntimeService().createProcessInstanceByKey("PaymentProcess")
                .businessKey(orderId)
                .setVariables(variables)
                .executeWithVariablesInReturn();
        VariableMap returnVariables = processInstance.getVariables();
        returnVariables.put("ProcessInstanceId", processInstance.getProcessInstanceId());
        return returnVariables;
    }

    @GetMapping("/message-start")
    public String messageStart(@RequestParam String orderId, @RequestParam String paymentOrderCode, @RequestParam String shippingRequestCode, @RequestParam String shippingOrderCode) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("OrderId", orderId);
        variables.put("paymentOrderCode", paymentOrderCode);
        variables.put("shippingRequestCode", shippingRequestCode);
        variables.put("shippingOrderCode", shippingOrderCode);

        log.info("messageStart, {}, {}", orderId, variables);
        ProcessInstance processInstance = processEngine.getRuntimeService().startProcessInstanceByMessage("PaymentProcess.Start", orderId, variables);
        return processInstance.getProcessInstanceId();
    }

    @GetMapping("/info-update")
    public void paymentInfoUpdate(@RequestParam String orderId) {
        log.info("paymentInfoUpdate, {}", orderId);
        processEngine.getRuntimeService().correlateMessage("PaymentProcess.InfoUpdate", orderId);
    }

    @GetMapping("/package-received")
    public void packageReceived(@RequestParam String executionId) {
        log.info("packageReceived, {}", executionId);
        processEngine.getRuntimeService().messageEventReceived("PaymentProcess.PackageReceived", executionId);
    }

    @GetMapping("/cancel")
    public void paymentCancel(@RequestParam String executionId) {
        log.info("paymentCancel, {}", executionId);
        processEngine.getRuntimeService().messageEventReceived("PaymentProcess.Cancel", executionId);
    }
}
