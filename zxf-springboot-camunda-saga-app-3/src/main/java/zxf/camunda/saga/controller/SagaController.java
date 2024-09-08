package zxf.camunda.saga.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import zxf.camunda.saga.saga.App3Saga;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@RestController
public class SagaController {
    private final AtomicInteger counter = new AtomicInteger();
    @Autowired
    private App3Saga app3Saga;

    @GetMapping("/saga/app-3")
    public void app3(@RequestParam Integer count) {
        String prefix = "app3@zxf-app-3";
        log.info("Trigger {} saga start, {}", prefix, count);
        app3Saga.trigger(prefix, counter.incrementAndGet(), count);
    }
}