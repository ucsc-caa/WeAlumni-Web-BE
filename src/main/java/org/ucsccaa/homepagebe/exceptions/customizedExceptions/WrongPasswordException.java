package org.ucsccaa.homepagebe.exceptions.customizedExceptions;

import org.ucsccaa.homepagebe.exceptions.ExceptionHandler;
import org.ucsccaa.homepagebe.exceptions.GenericServiceException;

public class WrongPasswordException extends GenericServiceException {
    public WrongPasswordException(String identifier) {
        super(ExceptionHandler.WRONG_PASSWORD.setMessage("Wrong Password: user - " + identifier),
                "Wrong Password: user - " + identifier);
    }
}
