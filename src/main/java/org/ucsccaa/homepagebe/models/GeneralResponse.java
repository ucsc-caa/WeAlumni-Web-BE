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
    private T body;

    public GeneralResponse(T body) {
        this(200, body);
    }

    public GeneralResponse(int code, T body) {
        this.code = code;
        this.body = body;
    }
}
