package org.ucsccaa.homepagebe.exceptions.customizedExceptions;

import org.ucsccaa.homepagebe.exceptions.ExceptionHandler;
import org.ucsccaa.homepagebe.exceptions.GenericServiceException;

public class BadResourceException extends GenericServiceException {
    public BadResourceException(String reason) {
        super(ExceptionHandler.BAD_REQUEST.setMessage("Bad Resource: reason - " + reason),
                "Bad Resource: reason - " + reason);
    }
}
