package com.acme.ecommerce.user.core.businessexception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    SUCCESS("USR-0000", "Success", HttpStatus.OK),
    NOT_FOUND("USR-0001", "User not found", HttpStatus.NOT_FOUND),
    ALREADY_EXISTS("USR-0002", "User already exists", HttpStatus.CONFLICT),
    INVALID_REQUEST("USR-0003", "Invalid request", HttpStatus.BAD_REQUEST),
    ROLE_NOT_FOUND("USR-0004", "Role not found", HttpStatus.NOT_FOUND),
    ROLE_ALREADY_EXISTS("USR-0005", "Role already exists", HttpStatus.CONFLICT),
    ROLE_ALREADY_ASSIGNED("USR-0006", "Role already assigned to user", HttpStatus.CONFLICT),
    ROLE_NOT_ASSIGNED("USR-0007", "Role not assigned to user", HttpStatus.NOT_FOUND),
    SERVER_ERROR("USR-0099", "Server error", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
    @Setter
    private Object payloadError;

    ErrorCode(String code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public static ErrorCode from(ErrorCode errorCode, Object payloadError) {
        errorCode.setPayloadError(payloadError);
        return errorCode;
    }

    public static ErrorCode from(String errorCode, ErrorCode defaultErrorCode) {
        for (ErrorCode e : ErrorCode.values()) {
            if (e.getCode().equals(errorCode)) {
                return e;
            }
        }
        return defaultErrorCode;
    }
}
