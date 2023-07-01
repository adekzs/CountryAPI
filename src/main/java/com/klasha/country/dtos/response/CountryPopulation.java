package com.klasha.country.dtos.response;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class CountryPopulation {

    private String country;
    private List<PopulationInfo> populationCounts;

    @Data
    @ToString
    public static class PopulationInfo {
        private double year;
        private double value;
    }
}
