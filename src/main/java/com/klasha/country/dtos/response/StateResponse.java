package com.klasha.country.dtos.response;

import lombok.Data;

import java.util.List;

@Data
public class StateResponse {

    private List<State> states;

    @Data
    public static class State {
        private String name;
    }
}
