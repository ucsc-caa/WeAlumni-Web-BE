package org.ucsccaa.homepagebe.exceptions.customizedExceptions;

import org.ucsccaa.homepagebe.exceptions.ExceptionHandler;
import org.ucsccaa.homepagebe.exceptions.GenericServiceException;

public class AuthenticationHackedException extends GenericServiceException {
    public AuthenticationHackedException(String token) {
        super(ExceptionHandler.AUTHENTICATION_HACKED.setMessage("Authentication Hacked: token - " + token),
                "Authentication Hacked: token - " + token);
    }
}