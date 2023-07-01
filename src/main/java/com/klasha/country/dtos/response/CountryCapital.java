package com.klasha.country.dtos.response;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CountryCapital {
    private String name;
    private String capital;
    private String iso2;
    private String iso3;
}
