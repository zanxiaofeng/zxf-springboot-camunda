package zxf.camunda.saga.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import zxf.camunda.saga.saga.App2Saga;
import zxf.camunda.saga.saga.CommonSaga;

@Slf4j
@RestController
public class SagaController {
    @Autowired
    private CommonSaga commonSaga;
    @Autowired
    private App2Saga app1Saga;

    @GetMapping("/saga/common")
    public void common(@RequestParam Integer count) {
        log.info("Trigger zxf-common-1 saga start, " + count);
        commonSaga.trigger(count);
    }

    @GetMapping("/saga/app-2")
    public void app1(@RequestParam Integer count) {
        log.info("Trigger zxf-app-2 saga start, " + count);
        app1Saga.trigger(count);
    }
}