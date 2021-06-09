package org.ucsccaa.homepagebe.exceptions.customizedExceptions;

import org.ucsccaa.homepagebe.exceptions.ExceptionHandler;
import org.ucsccaa.homepagebe.exceptions.GenericServiceException;

public class NoReadAccessException extends GenericServiceException {
    public NoReadAccessException(String identifier, String httpMethod, String url) {
        super(ExceptionHandler.NO_READ_ACCESS.setMessage("No Read Access: user - " + identifier),
                "No Read Access: user - ".concat(identifier).concat(", request - ".concat(httpMethod).concat(" ").concat(url)));
    }
}
