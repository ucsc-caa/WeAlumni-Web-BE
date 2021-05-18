package org.ucsccaa.homepagebe.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeneralResponse<T> {
    private int code;
    private String message;
    private T payload;

    public GeneralResponse(T payload) {
        this(200, payload);
    }

    public GeneralResponse(int code, T payload) {
        this.code = code;
        this.payload = payload;
    }
}
