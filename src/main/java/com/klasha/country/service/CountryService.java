package com.klasha.country.service;

import com.klasha.country.dtos.request.CurrencyInfo;
import com.klasha.country.dtos.response.CityAndStates;
import com.klasha.country.dtos.response.CountryInfoResponse;
import com.klasha.country.dtos.response.Currency;
import com.klasha.country.dtos.response.TopCities;


public interface CountryService {

    CountryInfoResponse getInformation(String country);

    CityAndStates getStatesAndCities(String country);

    TopCities getTopCities(int noOfCities);

    Currency convertCurrency(String country, CurrencyInfo info);
}
