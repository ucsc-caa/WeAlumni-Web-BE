package org.ucsccaa.homepagebe.exceptions.customizedExceptions;

import org.ucsccaa.homepagebe.exceptions.ExceptionHandler;
import org.ucsccaa.homepagebe.exceptions.GenericServiceException;

public class UserNotFoundException extends GenericServiceException {
    public UserNotFoundException(String identifier) {
        super(ExceptionHandler.USER_NOT_FOUND.setMessage("User Not Found: user - " + identifier),
                "User Not Found: user - " + identifier);
    }
}
