package com.klasha.country.dtos.global;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ISO {
    private String iso2;
    private String iso3;
}