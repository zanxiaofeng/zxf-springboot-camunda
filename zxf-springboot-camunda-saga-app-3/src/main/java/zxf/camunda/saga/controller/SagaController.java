package zxf.camunda.saga.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import zxf.camunda.saga.saga.App1Saga;
import zxf.camunda.saga.saga.App2Saga;
import zxf.camunda.saga.saga.App3Saga;
import zxf.camunda.saga.saga.ByIdSaga;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@RestController
public class SagaController {
    private final AtomicInteger counter = new AtomicInteger();
    @Autowired
    private App1Saga app1Saga;
    @Autowired
    private App2Saga app2Saga;
    @Autowired
    private App3Saga app3Saga;
    @Autowired
    private ByIdSaga byIdSaga;

    @GetMapping("/saga/app-1")
    public void app1(@RequestParam Integer count) {
        String prefix = "app1@zxf-app-3";
        log.info("Trigger {} saga start, {}", prefix, count);
        app1Saga.trigger(prefix, counter.incrementAndGet(), count);
    }

    @GetMapping("/saga/app-2")
    public void app2(@RequestParam Integer count) {
        String prefix = "app2@zxf-app-3";
        log.info("Trigger {} saga start, {}", prefix, count);
        app2Saga.trigger(prefix, counter.incrementAndGet(), count);
    }

    @GetMapping("/saga/app-3")
    public void app3(@RequestParam Integer count) {
        String prefix = "app3@zxf-app-3";
        log.info("Trigger {} saga start, {}", prefix, count);
        app3Saga.trigger(prefix, counter.incrementAndGet(), count);
    }

    @GetMapping("/saga/byId")
    public void byId(@RequestParam Integer count, @RequestParam String processDefinitionId) {
        log.info("Trigger byId saga start, {}, {}", processDefinitionId, count);
        byIdSaga.trigger(processDefinitionId, counter.incrementAndGet(), count);
    }
}