package org.ucsccaa.homepagebe.exceptions.customizedExceptions;

import org.ucsccaa.homepagebe.exceptions.ExceptionHandler;
import org.ucsccaa.homepagebe.exceptions.GenericServiceException;

public class AuthenticationRequiredException extends GenericServiceException {
    public AuthenticationRequiredException(String message) {
        super(ExceptionHandler.AUTHENTICATION_REQUIRED.setMessage("Authentication Required: message - " + message),
                "Authentication Required: message - " + message);
    }

    public AuthenticationRequiredException() {
        super(ExceptionHandler.AUTHENTICATION_REQUIRED, "Authentication Required");
    }
}
