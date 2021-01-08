package org.ucsccaa.homepagebe.models;

public enum Status {
    SUCCESS(200),
    ERROR(500),
    NOT_FOUND(404);

    private int response;

    Status(int response) {
        this.response = response;
    }

    public int getResponse() {
        return this.response;
    }

    public void setResponse(int response) {
        this.response = response;
    }
}