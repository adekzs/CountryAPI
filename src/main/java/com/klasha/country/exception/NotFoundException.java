package com.klasha.country.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@EqualsAndHashCode(callSuper = true)
@Data
public class NotFoundException extends RuntimeException {

    private final String message;
    private final HttpStatus status;

    public NotFoundException(String message) {
        this.message = message;
        this.status = HttpStatus.NOT_FOUND;
    }

    @Override
    public String getLocalizedMessage() {
        return this.message;
    }
}
