package com.acme.ecommerce.user.api.config;

import com.acme.ecommerce.user.api.utils.ResponseApi;
import com.acme.ecommerce.user.core.businessexception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseApi> handleValidationException(MethodArgumentNotValidException ex) {
        log.warn("Validation error: [{}]", ex.getMessage());

        Map<String, String> errors = new HashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return ResponseEntity.status(ErrorCode.INVALID_REQUEST.getHttpStatus())
                .body(ResponseApi.error(
                        ErrorCode.INVALID_REQUEST.getCode(),
                        ErrorCode.INVALID_REQUEST.getMessage(),
                        errors));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseApi> handleGenericException(Exception ex) {
        log.error("Unexpected error: [{}]", ex.getMessage(), ex);

        return ResponseEntity.status(ErrorCode.SERVER_ERROR.getHttpStatus())
                .body(ResponseApi.error(
                        ErrorCode.SERVER_ERROR.getCode(),
                        ErrorCode.SERVER_ERROR.getMessage()));
    }
}
