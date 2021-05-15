package org.ucsccaa.homepagebe.exceptions.customizedExceptions;

import org.ucsccaa.homepagebe.exceptions.ExceptionHandler;
import org.ucsccaa.homepagebe.exceptions.GenericServiceException;

public class AuthenticationExpiredException extends GenericServiceException {
    public AuthenticationExpiredException(String token) {
        super(ExceptionHandler.AUTHENTICATION_EXPIRED.setMessage("Authentication Expired: token - " + token),
                "Authentication Expired: token - " + token);
    }
}