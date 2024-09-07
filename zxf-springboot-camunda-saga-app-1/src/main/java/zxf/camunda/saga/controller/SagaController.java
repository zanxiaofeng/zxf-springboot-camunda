package zxf.camunda.saga.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import zxf.camunda.saga.saga.App1Saga;
import zxf.camunda.saga.saga.CommonSaga;


@Slf4j
@RestController
public class SagaController {
    @Autowired
    private CommonSaga commonSaga;
    @Autowired
    private App1Saga appSaga;

    @GetMapping("/saga/common")
    public void common(@RequestParam Integer count) {
        log.info("Trigger zxf-common saga start, {}", count);
        commonSaga.trigger(null, count);
    }

    @GetMapping("/saga/app-1")
    public void app(@RequestParam Integer count) {
        log.info("Trigger zxf-app-1 saga start, {}", count);
        appSaga.trigger(null, count);
    }

    @GetMapping("/saga/byId")
    public void byId(@RequestParam Integer count, @RequestParam String processDefinitionId) {
        log.info("Trigger saga byId start, {}, {}", processDefinitionId, count);
        appSaga.trigger(processDefinitionId, count);
    }
}