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
public class CityAndStates {

    private List<State> states;
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class State {
        private String state;
        private List<String> cities;
    }
}
