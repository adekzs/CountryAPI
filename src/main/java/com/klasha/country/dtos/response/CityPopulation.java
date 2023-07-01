package com.klasha.country.dtos.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class CityPopulation {

    private String city;
    List<City> populationCounts;

    @Data
    public static class City {

        private String year;
        @JsonProperty("value")
        private String population;

    }
}
