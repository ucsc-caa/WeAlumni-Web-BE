package org.ucsccaa.homepagebe.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.ucsccaa.homepagebe.models.GeneralResponse;

public enum ExceptionHandler {
    USER_NOT_FOUND(HttpStatus.OK, 2001, "USER NOT FOUND"),
    WRONG_PASSWORD(HttpStatus.OK, 2002, "WRONG PASSWORD"),
    USERS_EXISTS(HttpStatus.OK, 2003, "USER EXISTS"),
    VERIFICATION_CODE_EXPIRED(HttpStatus.OK, 2004, "VERIFICATION CODE EXPIRED"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, 4001, "BAD REQUEST"),
    REQUIRED_FIELD_IS_NULL(HttpStatus.BAD_REQUEST, 4002, "REQUIRED FIELD IS NULL"),
    AUTHENTICATION_REQUIRED(HttpStatus.UNAUTHORIZED, 4101, "AUTHENTICATION REQUIRED"),
    AUTHENTICATION_HACKED(HttpStatus.UNAUTHORIZED, 4102, "AUTHENTICATION HACKED"),
    AUTHENTICATION_EXPIRED(HttpStatus.UNAUTHORIZED, 4103, "AUTHENTICATION EXPIRED"),
    NO_READ_ACCESS(HttpStatus.FORBIDDEN, 4301, "NO READ ACCESS"),
    NO_WRITE_ACCESS(HttpStatus.FORBIDDEN, 4302, "NO WRITE ACCESS"),
    NO_EXECUTE_ACCESS(HttpStatus.FORBIDDEN, 4303, "NO EXECUTE ACCESS"),
    RESOURCE_DELETED(HttpStatus.GONE, 410, "RESOURCE DELETED"),
    SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 500, "UNKNOWN INTERNAL ERROR");

    private final HttpStatus httpStatusCode;
    private final int internalStatusCode;
    private String message;

    ExceptionHandler(HttpStatus httpStatusCode, int internalStatusCode, String message) {
        this.httpStatusCode = httpStatusCode;
        this.internalStatusCode = internalStatusCode;
        this.message = message;
    }

    public ExceptionHandler setMessage(String message) {
        this.message = message;
        return this;
    }

    public Integer getStatusCodeValue() {
        return this.httpStatusCode.value();
    }

    public ResponseEntity<GeneralResponse> getResponseEntity() {
        return new ResponseEntity<>(
                new GeneralResponse<>(this.internalStatusCode, this.message, null),
                this.httpStatusCode);
    }
}
