package zxf.camunda.arch.app.exception;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class BusinessErrorException extends Exception {
    private final String businessErrorCode;

    public BusinessErrorException(String businessErrorCode, String message) {
        super(message);
        this.businessErrorCode = businessErrorCode;
    }

    public Map<String, Object> response() {
        Map<String, Object> response = new HashMap<>();
        response.put("code", this.businessErrorCode);
        response.put("message", this.getMessage());
        return response;
    }
}
