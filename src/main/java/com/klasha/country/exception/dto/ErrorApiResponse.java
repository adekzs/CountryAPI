package com.klasha.country.exception.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ErrorApiResponse {
    private boolean error;
    private String msg;
}
