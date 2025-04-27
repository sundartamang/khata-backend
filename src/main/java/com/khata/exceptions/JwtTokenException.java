package com.khata.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class JwtTokenException extends RuntimeException {

    private final HttpStatus httpStatus;
    private final int statusCode;

    public JwtTokenException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
        this.statusCode = httpStatus.value();
    }
}