package org.ucsccaa.homepagebe.exceptions.customizedExceptions;

import org.ucsccaa.homepagebe.exceptions.ExceptionHandler;
import org.ucsccaa.homepagebe.exceptions.GenericServiceException;

public class UserExistException extends GenericServiceException {
    public UserExistException(String identifier) {
        super(ExceptionHandler.USERS_EXISTS.setMessage("User Already Exists: user - " + identifier),
                "User Already Exists: user - " + identifier);
    }
}
