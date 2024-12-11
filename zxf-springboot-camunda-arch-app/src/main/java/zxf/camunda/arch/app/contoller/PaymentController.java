package zxf.camunda.arch.app.contoller;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@Slf4j
@RestController
@RequestMapping("/payment")
public class PaymentController {
    @Autowired
    ProcessEngine processEngine;

    @GetMapping("/normal-start")
    public String normalStart(@RequestParam String orderId) {
        log.info("normalStart, {}", orderId);
        ProcessInstance processInstance = processEngine.getRuntimeService().startProcessInstanceByKey("PaymentProcess", orderId, Collections.singletonMap("OrderId", orderId));
        return processInstance.getProcessInstanceId();
    }

    @GetMapping("/message-start")
    public String messageStart(@RequestParam String orderId) {
        log.info("messageStart, {}", orderId);
        ProcessInstance processInstance = processEngine.getRuntimeService().startProcessInstanceByMessage("PaymentProcess.Start", orderId, Collections.singletonMap("OrderId", orderId));
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
