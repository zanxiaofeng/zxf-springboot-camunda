package zxf.camunda.arch.app;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.camunda.bpm.spring.boot.starter.event.PostDeployEvent;
import org.camunda.bpm.spring.boot.starter.event.PreUndeployEvent;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.event.EventListener;

@Slf4j
@SpringBootApplication
@EnableProcessApplication
public class Application {

    public static void main(String... args) {
        SpringApplication.run(Application.class, args);
    }

    @EventListener
    public void onPostDeploy(PostDeployEvent event) {
        log.info("on PostDeploy: {}", event);
    }

    @EventListener
    public void onPreUndeploy(PreUndeployEvent event) {
        log.info("on PreUndeploy: {}", event);
    }
}