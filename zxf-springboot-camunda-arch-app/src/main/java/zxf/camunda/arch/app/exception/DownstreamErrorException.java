package zxf.camunda.arch.app.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DownstreamErrorException extends Exception {
    private final int downStreamStatusCode;
    private final String downStreamErrorCode;
}
