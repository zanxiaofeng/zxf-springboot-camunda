package zxf.camunda.arch.app.advice;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import zxf.camunda.arch.app.exception.BusinessErrorException;
import zxf.camunda.arch.app.exception.DownstreamErrorException;

import java.util.Map;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessErrorException.class)
    public ResponseEntity<Map<String, Object>> handleBusinessErrorException(BusinessErrorException businessErrorException) {
        log.error("BusinessErrorException ", businessErrorException);
        return ResponseEntity.internalServerError().body(businessErrorException.response());
    }

    @ExceptionHandler(DownstreamErrorException.class)
    public ResponseEntity<Map<String, Object>> handleBusinessErrorException(DownstreamErrorException downstreamErrorException) {
        log.error("DownstreamErrorException ", downstreamErrorException);
        return ResponseEntity.internalServerError().body(downstreamErrorException.response());
    }
}
