package org.ucsccaa.homepagebe.models;

import lombok.Data;

@Data
public class GeneralResponse<T> {
    private int code;
    private String message;
    private T payload;

    public GeneralResponse() {
        this(200, "success", null);
    }

    public GeneralResponse(T payload) {
        this(200, "success", payload);
    }

    public GeneralResponse(int code, String message, T payload) {
        this.code = code;
        this.message = message;
        this.payload = payload;
    }
}
