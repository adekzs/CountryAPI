package com.klasha.country.dtos.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CityRequest {

    private String country;
    private String state;

}
