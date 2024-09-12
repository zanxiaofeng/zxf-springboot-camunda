package zxf.camunda.saga.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import zxf.camunda.saga.saga.*;
import zxf.camunda.saga.service.CamundaService;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@RestController
public class SagaController {
    private final AtomicInteger counter = new AtomicInteger(0);
    @Autowired
    private App1Saga app1Saga;
    @Autowired
    private App2Saga app2Saga;
    @Autowired
    private App3Saga app3Saga;
    @Autowired
    private App4Saga app4Saga;
    @Autowired
    private ByIdSaga byIdSaga;
    @Autowired
    private CamundaService camundaService;

    @GetMapping("/saga/all")
    public String all(@RequestParam Integer count, @RequestParam(required = false) Integer start) {
        int times = counter.addAndGet(10);
        start = Optional.ofNullable(start).orElse(10000);
        log.info("Trigger all@{} saga start, {}::{}-{}", camundaService.appName(), times, start, count);
        for (int number = start; number < start + count; number++) {
            app1Saga.createInstance(times, number);
            app2Saga.createInstance(times, number);
            app3Saga.createInstance(times, number);
            app4Saga.createInstance(times, number);
        }
        log.info("Trigger all@{} saga end, {}::{}-{}", camundaService.appName(), times, start, count);
        return LocalDateTime.now().toString();
    }

    @GetMapping("/saga/app-1")
    public String app1(@RequestParam Integer count, @RequestParam(required = false) Integer start) {
        log.info("Trigger {} saga start, {}", app1Saga.getPrefix(), count);
        return app1Saga.trigger(counter.addAndGet(10) + 1, count, start);
    }

    @GetMapping("/saga/app-2")
    public String app2(@RequestParam Integer count, @RequestParam(required = false) Integer start) {
        log.info("Trigger {} saga start, {}", app2Saga.getPrefix(), count);
        return app2Saga.trigger(counter.addAndGet(10) + 2, count, start);
    }

    @GetMapping("/saga/app-3")
    public String app3(@RequestParam Integer count, @RequestParam(required = false) Integer start) {
        log.info("Trigger {} saga start, {}", app3Saga.getPrefix(), count);
        return app3Saga.trigger(counter.addAndGet(10) + 3, count, start);
    }

    @GetMapping("/saga/app-4")
    public String app4(@RequestParam Integer count, @RequestParam(required = false) Integer start) {
        log.info("Trigger {} saga start, {}", app4Saga.getPrefix(), count);
        return app4Saga.trigger(counter.addAndGet(10) + 4, count, start);
    }

    @GetMapping("/saga/byId")
    public String byId(@RequestParam Integer count, @RequestParam String processDefinitionId, @RequestParam(required = false) Integer start) {
        log.info("Trigger byId saga start, {}, {}", processDefinitionId, count);
        return byIdSaga.trigger(processDefinitionId, counter.addAndGet(10) + 4, count, start);
    }
}