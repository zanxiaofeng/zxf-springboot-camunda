package zxf.camunda.saga.saga;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class Startup {
    @Autowired
    private App2Saga app2Saga;
    @Autowired
    private App4Saga app4Saga;

    @PostConstruct
    public void startUp() {
        app2Saga.deploySaga();
        app4Saga.deploySaga();
    }
}
