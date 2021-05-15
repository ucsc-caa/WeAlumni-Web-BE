package org.ucsccaa.homepagebe.exceptions;

public class GenericServiceException extends RuntimeException {
    private ExceptionHandler exceptionHandler;
    public GenericServiceException() {
        super();
    }
    public GenericServiceException(ExceptionHandler exceptionHandler, String message) {
        super(message);
    }

    public ExceptionHandler getExceptionHandler() {
        return exceptionHandler;
    }
}
