package com.klasha.country.dtos.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Builder
public class CountryLocation {

    private String name;
    private double lat;
    @JsonProperty("long")
    private double log;

}
