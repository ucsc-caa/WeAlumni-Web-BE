package org.ucsccaa.homepagebe.models;

import lombok.Data;

@Data
public class ServiceResponse<T> {
    private Status status;
    private String errorMessage;
    private T payload;

    public ServiceResponse() {
        this.status = Status.SUCCESS;
    }

    public ServiceResponse(Status status, String errorMessage, T payload) {
        this.status = status;
        this.errorMessage = errorMessage;
        this.payload = payload;
    }

    public ServiceResponse(Status status, String errorMessage) {
        this.status = status;
        this.errorMessage = errorMessage;
    }

    public ServiceResponse(T payload) {
        this.status = Status.SUCCESS;
        this.payload = payload;
    }
}