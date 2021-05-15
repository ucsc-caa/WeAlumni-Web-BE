package org.ucsccaa.homepagebe.exceptions.customizedExceptions;

import org.ucsccaa.homepagebe.exceptions.ExceptionHandler;
import org.ucsccaa.homepagebe.exceptions.GenericServiceException;

public class RequiredFieldIsNullException extends GenericServiceException {
    public RequiredFieldIsNullException(String fieldName) {
        super(ExceptionHandler.REQUIRED_FIELD_IS_NULL.setMessage("Required Field is null: field - " + fieldName),
                "Required Field is null: field - " + fieldName);
    }
}