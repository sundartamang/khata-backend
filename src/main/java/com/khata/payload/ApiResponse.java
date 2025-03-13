package com.khata.payload;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ApiResponse<T> {
    private T data;
    private Integer statusCode;
    private String message;

    public ApiResponse(T data,Integer statusCode, String message) {
        this.data = data;
        this.statusCode = statusCode;
        this.message = message;
    }

    public ApiResponse(T data, Integer statusCode) {
        this.data = data;
        this.statusCode = statusCode;
        this.message = null;
    }
}
