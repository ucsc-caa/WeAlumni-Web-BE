package org.ucsccaa.homepagebe.exceptions.customizedExceptions;

import org.ucsccaa.homepagebe.exceptions.ExceptionHandler;
import org.ucsccaa.homepagebe.exceptions.GenericServiceException;

public class ResourceDeletedException extends GenericServiceException {
    public ResourceDeletedException(String resource) {
        super(ExceptionHandler.RESOURCE_DELETED.setMessage("Resource Deleted: resource - " + resource),
                "Resource Deleted: resource - " + resource);
    }
}
