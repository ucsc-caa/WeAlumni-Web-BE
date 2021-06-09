package org.ucsccaa.homepagebe.exceptions.customizedExceptions;

import org.ucsccaa.homepagebe.exceptions.ExceptionHandler;
import org.ucsccaa.homepagebe.exceptions.GenericServiceException;

public class NoExecuteAccessException extends GenericServiceException {
    public NoExecuteAccessException(String identifier, String httpMethod, String url) {
        super(ExceptionHandler.NO_EXECUTE_ACCESS.setMessage("No Execute Access: user - " + identifier),
                "No Execute Access: user - ".concat(identifier).concat(", request - ".concat(httpMethod).concat(" ").concat(url)));
    }
}
