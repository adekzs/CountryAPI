package com.klasha.country.service.impl;

import com.klasha.country.dtos.request.CurrencyInfo;
import com.klasha.country.dtos.response.CityAndStates;
import com.klasha.country.dtos.response.CountryInfoResponse;
import com.klasha.country.dtos.response.Currency;
import com.klasha.country.dtos.response.TopCities;
import com.klasha.country.service.CountryService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.klasha.country.dtos.response.TopCities.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Log4j2
class CountryServiceImplTest {

    @Autowired
    private  CountryService countryService;
    @Test
    void testgetCountryInformation() {
        CountryInfoResponse location = countryService.getInformation("Nigeria");
        assertNotNull(location.getLocation(), "location Should not be null");
        assertNotNull(location.getIso(),"ISO should not be null");
        assertNotNull(location.getCurrency(),"Currency should be present");
        assertNotNull(location.getCapitalCity(), "city should be present");
    }

    @Test
    void testgetStatesAndCities() {
        CityAndStates statesAndCities = countryService.getStatesAndCities("Nigeria");
        assertNotNull(statesAndCities.getStates(), "States should not be null");
        assertTrue(statesAndCities.getStates().size() > 0, "states should be more than 0");
        for (var city : statesAndCities.getStates()) {
            assertNotNull(city.getCities(), "Cities should not be null");
        }
    }

    @Test
    void testgetTopCities() {
        int noOfCities = 5;
        TopCities topCities = countryService.getTopCities(noOfCities);
        assertNotNull(topCities.getGhana());
        assertNotNull(topCities.getItaly());
        assertNotNull(topCities.getNewZealand());
    }

    @Test
    void testgetTopCitiesNumberOfCitiesReturnedisLessThanCitiesRequested() {
        testNumberOfCities(5);
        testNumberOfCities(1);
        testNumberOfCities(12);
        testNumberOfCities(0);
    }

    private void testNumberOfCities(int noOfCities) {
        TopCities topCities = countryService.getTopCities(noOfCities);
        assertNotNull(topCities.getGhana());
        assertNotNull(topCities.getItaly());
        assertNotNull(topCities.getNewZealand());

        assertTrue(topCities.getGhana().getCities().size() <= noOfCities);
        assertTrue(topCities.getItaly().getCities().size() <= noOfCities);
        assertTrue(topCities.getNewZealand().getCities().size() <= noOfCities);
    }


    @Test
    void testGetCitiesAndCheckIfSorted() {
        TopCities topCities = countryService.getTopCities(10);
        assertNotNull(topCities.getGhana());
        assertNotNull(topCities.getItaly());
        assertNotNull(topCities.getNewZealand());
        checkIfCitiesAreSorted(topCities.getGhana(), "Ghana");
        checkIfCitiesAreSorted(topCities.getItaly(), "Italy");
        checkIfCitiesAreSorted(topCities.getNewZealand(), "newZealand");
    }

    private static void checkIfCitiesAreSorted(City topCities, String country) {
        log.info(topCities);
        double lastPopulation = Double.MAX_VALUE;
        for (var cities: topCities.getCities()) {
            double currentPopulation = Double.parseDouble(cities.getPopulation());
            assertTrue(currentPopulation <= lastPopulation, "Population not properly sorted for "+country);
            lastPopulation = currentPopulation;
        }
    }

    @Test
    void testconvertCurrency() {
        Currency ss = countryService.convertCurrency("Nigeria", CurrencyInfo.builder()
                        .currency("JPY")
                        .amount(15000.0)
                .build());
        assertNotNull(ss.getAmount(), "Amount should not be null");
        log.info(ss.getAmount());
        assertNotNull(ss.getCountryCurr(), "Country Currenc should not be null");
    }

    @Test
    void testConvertInverseCurrency() {
        Currency ss = countryService.convertCurrency("Japan", CurrencyInfo.builder()
                .currency("NGN")
                .amount(15000.0)
                .build());
        assertNotNull(ss.getAmount(), "Amount should not be null");
        log.info(ss.getAmount());
        assertNotNull(ss.getCountryCurr(), "Country Currenc should not be null");
    }


}