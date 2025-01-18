package zxf.camunda.arch.app.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
public class DownstreamErrorException extends Exception {
    private final int downStreamStatusCode;
    private final String downStreamErrorCode;

    public Map<String, Object> response() {
        Map<String, Object> response = new HashMap<>();
        response.put("code", BusinessErrors.APP_DOWNSTREAM_003.getCode());
        response.put("message", BusinessErrors.APP_DOWNSTREAM_003.getDescription() + downStreamStatusCode + ", " + downStreamErrorCode);
        return response;
    }
}
