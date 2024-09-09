package zxf.camunda.saga.saga;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class Startup {
    @Autowired
    private App1Saga app1Saga;

    @Autowired
    private App2Saga app2Saga;

    @PostConstruct
    public void startUp() {
        app1Saga.deploySaga();
        app2Saga.deploySaga();
    }
}
