package com.klasha.country.dtos.response;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CountryCurrency {
    private String name;
    private String currency;
    private String iso2;
    private String iso3;
}
