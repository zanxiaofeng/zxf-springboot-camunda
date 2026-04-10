package zxf.camunda.saga.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import zxf.camunda.saga.saga.ExtSaga;
import zxf.camunda.saga.service.CamundaService;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ExtController {
    private final AtomicInteger counter = new AtomicInteger(0);
    private final ExtSaga extSaga;
    private final CamundaService camundaService;

    @GetMapping("/saga/ext")
    public String ext(@RequestParam Integer count, @RequestParam(required = false) Integer start) {
        log.info("Trigger {} saga start, {}", extSaga.getPrefix(), count);
        return extSaga.trigger(counter.addAndGet(10) + 5, count, start);
    }
}
