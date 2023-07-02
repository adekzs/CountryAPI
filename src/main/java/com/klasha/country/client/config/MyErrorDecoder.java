package com.klasha.country.client.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.klasha.country.exception.BadRequestException;
import com.klasha.country.exception.CustomException;
import com.klasha.country.exception.NotFoundException;
import com.klasha.country.exception.dto.ErrorResponse;
import feign.FeignException;
import feign.Response;
import feign.RetryableException;
import feign.codec.ErrorDecoder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.io.InputStream;

@RequiredArgsConstructor
public class MyErrorDecoder implements ErrorDecoder {

    private final ObjectMapper objectMapper;
    private ErrorDecoder errorDecoder = new Default();
    @Override
    public Exception decode(String s, Response response) {

        ErrorResponse message = null;
        try (InputStream bodyIs = response.body()
                .asInputStream()) {
            ObjectMapper mapper = new ObjectMapper();
            message = mapper.readValue(bodyIs, ErrorResponse.class);
        } catch (IOException e) {
            return new CustomException(e.getMessage());
        }
        switch (response.status()) {
            case 400:
                return new BadRequestException(message.getMsg() != null ? message.getMsg() : "Bad Request");
            case 404:
                return new NotFoundException(message.getMsg() != null ? message.getMsg() : "Not found");
            default:
                return errorDecoder.decode(s, response);
        }
    }
}
