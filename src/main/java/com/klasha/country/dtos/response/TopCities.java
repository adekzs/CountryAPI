package com.klasha.country.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopCities {

    private City italy;
    private City newZealand;
    private City ghana;
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class City {

        private List<CityInfo> cities;

    }

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CityInfo {
        private String name;
        private String population;
        private String year;
    }
}
