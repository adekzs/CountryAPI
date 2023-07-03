package com.klasha.country.utils;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UtilityTest {
    // FORMATTING TEST

    @Autowired
    private Utility utility;
    @Test
    void testReadCSVFile() {
        HashMap<String, Double> currencyMap = utility.readFileCSV();
        assertEquals(8, currencyMap.size());
        assertEquals(currencyMap.get("EUR-NGN"), 493.06, "Wrong rate added");
        assertEquals(currencyMap.get("USD-NGN"),460.72, "Wrong rate added");
        assertEquals(currencyMap.get("GBP-NGN"), 570.81,"Wrong rate added");
        assertEquals(currencyMap.get("EUR-UGX"),  4004.33, "Wrong rate added");
        assertEquals(currencyMap.get("JPY-NGN"), 3.28, "Wrong rate added");
        assertEquals(currencyMap.get("JPY-UGX"), 26.62, "Wrong rate added");
        assertEquals(currencyMap.get("GBP-UGX"), 4633.48, "Wrong rate added");
        assertEquals(currencyMap.get("USD-UGX"),3739.83, "Wrong rate added");
        
    }
}