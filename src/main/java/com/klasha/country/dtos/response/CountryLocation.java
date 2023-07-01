package com.klasha.country.dtos.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CountryLocation {

    private String name;
    private double lat;
    @JsonProperty("long")
    private double log;

}
