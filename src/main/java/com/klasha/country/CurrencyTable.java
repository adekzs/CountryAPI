package com.klasha.country;

import com.klasha.country.utils.Utility;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
@Data
public class CurrencyTable {

    private HashMap<String, Double> currency;
    @PostConstruct
    public void init() {
        currency = Utility.readFileCSV();
    }


}
