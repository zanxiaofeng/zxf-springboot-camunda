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
    private App2Saga appSaga;

    @GetMapping("/saga/common")
    public void common(@RequestParam Integer count, @RequestParam(required = false) String processDefinitionId) {
        log.info("Trigger zxf-common saga start, {}, {}", processDefinitionId, count);
        commonSaga.trigger(processDefinitionId, count);
    }

    @GetMapping("/saga/app-1")
    public void app(@RequestParam Integer count, @RequestParam(required = false) String processDefinitionId) {
        log.info("Trigger zxf-app-2 saga start, {}, {}", processDefinitionId, count);
        appSaga.trigger(processDefinitionId, count);
    }
}