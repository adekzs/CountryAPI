package com.klasha.country.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@EqualsAndHashCode(callSuper = true)
@Data
public class CustomException extends RuntimeException{

    private String message;
    private HttpStatus status;

    public CustomException(String message) {
        super(message);
        this.message = message;
    }

    public CustomException(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }
}
