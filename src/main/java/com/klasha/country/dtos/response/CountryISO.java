package com.klasha.country.dtos.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CountryISO {

    private String name;
    @JsonProperty("Iso2")
    private String iso2;
    @JsonProperty("Iso3")
    private String iso3;

}
