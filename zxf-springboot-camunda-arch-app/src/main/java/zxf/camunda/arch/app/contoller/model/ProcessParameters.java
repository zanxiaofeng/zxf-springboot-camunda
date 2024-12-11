package zxf.camunda.arch.app.contoller.model;

import lombok.Data;

import java.util.Map;

@Data
public class ProcessParameters {
    private String businessKey;
    Map<String, Object> variables;
}
