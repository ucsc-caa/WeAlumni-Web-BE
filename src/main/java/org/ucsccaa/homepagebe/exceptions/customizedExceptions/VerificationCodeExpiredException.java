package org.ucsccaa.homepagebe.exceptions.customizedExceptions;

import org.ucsccaa.homepagebe.exceptions.ExceptionHandler;
import org.ucsccaa.homepagebe.exceptions.GenericServiceException;

public class VerificationCodeExpiredException extends GenericServiceException {
    public VerificationCodeExpiredException(String identifier) {
        super(ExceptionHandler.VERIFICATION_CODE_EXPIRED.setMessage("Verification Code Already Expired: user - " + identifier),
                "Verification Code Already Expired: user - " + identifier);
    }
}