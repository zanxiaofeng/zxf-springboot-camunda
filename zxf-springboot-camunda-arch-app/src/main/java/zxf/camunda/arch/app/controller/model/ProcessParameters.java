package zxf.camunda.arch.app.controller.model;

import lombok.Data;

import java.util.Map;

@Data
public class ProcessParameters {
    private String businessKey;
    Map<String, Object> variables;
}
