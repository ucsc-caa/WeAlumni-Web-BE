package org.ucsccaa.homepagebe.exceptions.customizedExceptions;

import org.ucsccaa.homepagebe.exceptions.ExceptionHandler;
import org.ucsccaa.homepagebe.exceptions.GenericServiceException;

public class ServerErrorException extends GenericServiceException {
    public ServerErrorException(String message) {
        super(ExceptionHandler.SERVER_ERROR.setMessage("Server Error: message - " + message),
                "Server Error: message - " + message);
    }
}
