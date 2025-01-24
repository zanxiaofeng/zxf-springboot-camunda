package zxf.camunda.arch.app.contoller.app;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.variable.VariableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import zxf.camunda.arch.app.exception.BusinessErrorException;
import zxf.camunda.arch.app.service.CamundaService;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/app/form")
public class FormController {
    @Autowired
    CamundaService camundaService;

    @PostMapping("/start")
    public VariableMap start(@RequestBody Map<String, Object> requestBody) throws BusinessErrorException {
        String formId = UUID.randomUUID().toString();
        log.info("start, fromId: {}", formId);
        return camundaService.startProcessWithVariablesInReturn("Flow-Form-Process", formId, Collections.singletonMap("requestBody", requestBody));
    }

    @PostMapping("/message")
    public VariableMap message(@RequestParam String formId, @RequestParam String action, @RequestBody Map<String, Object> messageBody) throws BusinessErrorException {
        log.info("message, fromId: {}, action: {}", formId, action);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("messageAction", action);
        parameters.put("messageBody", messageBody);

        return camundaService.correlateMessageWithVariablesInReturn("FormProcess.message", formId, parameters);
    }
}
