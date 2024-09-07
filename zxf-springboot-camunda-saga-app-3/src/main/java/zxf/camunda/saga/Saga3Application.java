package zxf.camunda.saga;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
@EnableProcessApplication
public class Saga3Application {
    public static void main(String... args) {
        SpringApplication.run(Saga3Application.class, args);
    }
}