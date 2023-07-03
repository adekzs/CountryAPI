package com.klasha.country.utils;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;

@Component
@RequiredArgsConstructor
@Data
public class CurrencyTable {

    private final Utility utility;
    private HashMap<String, Double> currency;
    @PostConstruct
    public void init() throws IOException {
        currency = utility.readFileCSV();
    }


}
