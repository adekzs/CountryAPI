package com.klasha.country.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class BadRequestException extends RuntimeException {

    private final String message;
    private final HttpStatus status;

    public BadRequestException(String message) {
        this.message = message;
        this.status = HttpStatus.BAD_REQUEST;
    }


}
