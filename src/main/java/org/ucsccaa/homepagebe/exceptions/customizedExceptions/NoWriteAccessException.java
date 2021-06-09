package org.ucsccaa.homepagebe.exceptions.customizedExceptions;

import org.ucsccaa.homepagebe.exceptions.ExceptionHandler;
import org.ucsccaa.homepagebe.exceptions.GenericServiceException;

public class NoWriteAccessException extends GenericServiceException {
    public NoWriteAccessException(String identifier, String httpMethod, String url) {
        super(ExceptionHandler.NO_WRITE_ACCESS.setMessage("No Write Access: user - " + identifier),
                "No Write Access: user - ".concat(identifier).concat(", request - ".concat(httpMethod).concat(" ").concat(url)));
    }
}
