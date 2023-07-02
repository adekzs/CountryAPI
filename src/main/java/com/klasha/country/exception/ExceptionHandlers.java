package com.klasha.country.exception;

import com.klasha.country.dtos.global.ApiResponse;
import com.klasha.country.exception.dto.ErrorResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
@Log4j2
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(value = {CustomException.class})
    public ResponseEntity<ApiResponse<?>> handleCustomException(CustomException exception) {
        log.error(exception.getMessage());
        ApiResponse<ErrorResponse> response = ApiResponse.<ErrorResponse>builder()
                .error(true)
                .msg(exception.getMessage())
                .build();

    }

}
