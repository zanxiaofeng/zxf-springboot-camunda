package zxf.camunda.saga.task;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zxf.camunda.saga.service.OrderService;

import java.util.UUID;

@Slf4j
@Component
public class App1Task2Adapter implements JavaDelegate {

    @Autowired
    private OrderService orderService;

    public App1Task2Adapter() {
        log.info("ctor()");
    }

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String taskId = (String) execution.getVariable("task-id");
        log.info("start, " + taskId + ", " + execution.getId());

        Thread.sleep(20000);

        try {
            String orderId = UUID.randomUUID().toString();
            orderService.createOrder(orderId);
            log.info("createOrder, " + execution.getVariable("task-id") + ", " + orderId);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        log.info("end, " + taskId + ", " + execution.getId());
    }
}