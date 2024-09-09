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
public class SagaController extends BaseSagaController {
    @Override
    protected String getAppName() {
        return "zxf-app-1";
    }
}