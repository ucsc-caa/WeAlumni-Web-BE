package org.ucsccaa.homepagebe.exceptions.customizedExceptions;

import org.ucsccaa.homepagebe.exceptions.ExceptionHandler;
import org.ucsccaa.homepagebe.exceptions.GenericServiceException;

public class BadRequestException extends GenericServiceException {
    public BadRequestException(String message) {
        super(ExceptionHandler.BAD_REQUEST.setMessage("Bad Request: reason - " + message),
                "Bad Request: reason - " + message);
    }
}
