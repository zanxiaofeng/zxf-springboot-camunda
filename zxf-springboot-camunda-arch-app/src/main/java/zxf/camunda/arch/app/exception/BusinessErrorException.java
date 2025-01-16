package zxf.camunda.arch.app.exception;

import lombok.Getter;

@Getter
public class BusinessErrorException extends Exception {
    private final String businessErrorCode;

    public BusinessErrorException(String businessErrorCode, String message) {
        super(message);
        this.businessErrorCode = businessErrorCode;
    }
}
