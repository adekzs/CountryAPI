package com.klasha.country.exception;

import com.klasha.country.exception.dto.ErrorApiResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import java.net.UnknownHostException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@ControllerAdvice
@Log4j2
public class ExceptionHandlers {

    @ExceptionHandler(value = {CustomException.class})
    public ResponseEntity<ErrorApiResponse> handleCustomException(CustomException exception) {
        log.error(exception.getMessage());
        ErrorApiResponse response = ErrorApiResponse.builder()
                .error(true)
                .msg(exception.getMessage())
                .build();

        return ResponseEntity.status(exception.getStatus())
                .body(response);
    }

    @ExceptionHandler(value = {BadRequestException.class})
    public ResponseEntity<ErrorApiResponse> handleBadRequestException(BadRequestException exception) {
        log.error(exception.getMessage());
        ErrorApiResponse response = ErrorApiResponse.builder()
                .error(true)
                .msg(exception.getMessage())
                .build();

        return ResponseEntity.status(exception.getStatus())
                .body(response);
    }

    @ExceptionHandler(value = {NotFoundException.class})
    public ResponseEntity<ErrorApiResponse> handleNotFoundException(NotFoundException exception) {
        log.error(exception.getLocalizedMessage());
        ErrorApiResponse response = ErrorApiResponse.builder()
                .error(true)
                .msg(exception.getLocalizedMessage())
                .build();

        return ResponseEntity.status(exception.getStatus())
                .body(response);
    }
    @ExceptionHandler(value = {UnknownHostException.class})
    public ResponseEntity<ErrorApiResponse> handleNotFoundException(UnknownHostException exception) {
        log.error(exception.getMessage());
        ErrorApiResponse response = ErrorApiResponse.builder()
                .error(true)
                .msg(exception.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT)
                .body(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorApiResponse> handleConstraintViolationException(final ConstraintViolationException exception) {
        final Set<ConstraintViolation<?>> violations = exception.getConstraintViolations();
        final String errors =  violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(""));

        ErrorApiResponse response = ErrorApiResponse.builder()
                .error(true)
                .msg(errors)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorApiResponse> handleMessageNotReadableException(final HttpMessageNotReadableException exception) {
        final String error = exception.getMessage();
        ErrorApiResponse response = ErrorApiResponse.builder()
                .error(true)
                .msg(error)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorApiResponse> handleMethodArgumentViolationException(final MethodArgumentNotValidException exception) {
        final List<ObjectError> violations = exception.getAllErrors();
        final String errors =  violations.stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(""));

        ErrorApiResponse response = ErrorApiResponse.builder()
                .error(true)
                .msg(errors)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }


//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ErrorApiResponse> globalExceptionHandler(Exception ex, WebRequest request) {
//        ex.printStackTrace();
//        ErrorApiResponse response = ErrorApiResponse.builder()
//                .error(true)
//                .msg(ex.getMessage())
//                .build();
//
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
//
//    }



}
