package com.klasha.country.dtos.global;

import lombok.Data;

@Data
public class CountryAPIResponse<T> {
    private boolean error;
    private String msg;
    private T data;
}
