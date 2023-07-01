package com.klasha.country.service.impl;

import com.klasha.country.CurrencyTable;
import com.klasha.country.client.api.CountryAPI;
import com.klasha.country.dtos.global.ISO;
import com.klasha.country.dtos.global.Location;
import com.klasha.country.dtos.request.CityRequest;
import com.klasha.country.dtos.request.CountryRequest;
import com.klasha.country.dtos.request.CurrencyInfo;
import com.klasha.country.dtos.response.*;
import com.klasha.country.service.CountryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.klasha.country.client.constants.Constants.Country.*;

@RequiredArgsConstructor
@Service
@Log4j2
public class CountryServiceImpl implements CountryService {

    private final CountryAPI countryAPI;
    private final CurrencyTable currencyTable;

    @Override
    public CountryInfoResponse getLocation(String country) {

        CountryRequest req = new CountryRequest();
        req.setCountry(country);
        var countryLocation = countryAPI.getCountryLocation(req);
        var countryCurrency = countryAPI.getCountryCurrency(req);
        var countryPopulation = countryAPI.getCountryPopulation(req);
        var capitalCity = countryAPI.getCountryCapital(req);
        int size = Objects.requireNonNull(countryPopulation.getBody()).getData().getPopulationCounts().size();
        CountryInfoResponse resp = CountryInfoResponse.builder()
                .currency(Objects.requireNonNull(countryCurrency.getBody()).getData().getCurrency())
                .capitalCity(Objects.requireNonNull(capitalCity.getBody()).getData().getCapital())
                .location(Location.builder()
                        .lat(Objects.requireNonNull(countryLocation.getBody()).getData().getLat())
                        .log(countryLocation.getBody().getData().getLog()).
                        build())
                .population(countryPopulation.getBody().getData().getPopulationCounts().get(size - 1).getValue())
                .iso(ISO.builder()
                        .iso2(countryCurrency.getBody().getData().getIso2())
                        .iso3(countryCurrency.getBody().getData().getIso3()).build())
                .build();

        log.info(countryLocation);
        log.info(resp);
        return resp;
    }

    @Override
    public CityAndStates getStatesAndCities(String country) {
        CityAndStates cityAndStates = new CityAndStates(new ArrayList<>());
        CountryRequest req = new CountryRequest();
        req.setCountry(country);
        var states = countryAPI.getCountryStates(req);
        for (var state : Objects.requireNonNull(states.getBody()).getData().getStates()) {
            try {
                var cities = countryAPI.getStateCities(CityRequest.builder()
                        .country(country)
                        .state(state.getName())
                        .build());
                cityAndStates.getStates()
                        .add(CityAndStates.State.builder()
                                .cities(Objects.requireNonNull(cities.getBody()).getData())
                                .state(state.getName())
                                .build());
            } catch (Exception ex) {
                log.info("Erro fetching cities for " + state.getName());
                log.error(ex.getMessage());

            }
        }
        return cityAndStates;
    }

    @Override
    public TopCities getTopCities(int noOfCities) {

        TopCities response = TopCities.builder()
                .ghana(TopCities
                        .City
                        .builder()
                        .cities(new ArrayList<>())
                        .build())
                .italy(TopCities
                        .City
                        .builder()
                        .cities(new ArrayList<>())
                        .build())
                .newZealand(TopCities
                        .City
                        .builder()
                        .cities(new ArrayList<>())
                        .build())
                .build();

        CountryRequest ghana = CountryRequest
                .builder()
                .country(GHANA)
                .build();
        CountryRequest italy = CountryRequest
                .builder()
                .country(ITALY)
                .build();
        CountryRequest newZealand = CountryRequest
                .builder()
                .country(NEW_ZEALAND)
                .build();

        var ghanaCitiesPopulation = countryAPI.getCitiesPopulation(ghana);
        var italyCitiesPopulation = countryAPI.getCitiesPopulation(italy);
        var newZealandPopulation = countryAPI.getCitiesPopulation(newZealand);

        sortCitiesByPopulation(Objects.requireNonNull(ghanaCitiesPopulation.getBody()).getData());
        sortCitiesByPopulation(Objects.requireNonNull(italyCitiesPopulation.getBody()).getData());
        sortCitiesByPopulation(Objects.requireNonNull(newZealandPopulation.getBody()).getData());

        ghanaCitiesPopulation.getBody().getData().stream()
                .limit(noOfCities)
                .forEach(city -> response.getGhana().getCities().add(getCityInfoFromCity(city)));
        italyCitiesPopulation.getBody().getData().stream()
                .limit(noOfCities)
                .forEach(city -> response.getItaly().getCities().add(getCityInfoFromCity(city)));
        newZealandPopulation.getBody().getData().stream()
                .limit(noOfCities)
                .forEach(city -> response.getNewZealand().getCities().add(getCityInfoFromCity(city)));

        return response;
    }

    @Override
    public Currency convertCurrency(String country, CurrencyInfo info) {
        var currencyData = currencyTable.getCurrency();
        var currencyInfo = countryAPI.getCountryCurrency(CountryRequest
                .builder()
                .country(country)
                .build());
        String countryCurrency = Objects.requireNonNull(currencyInfo.getBody()).getData().getCurrency();
        String countryCurrency2 = countryCurrency+"-"+info.getCurrency();
        double rate = currencyData.get(countryCurrency2);
        double convertedAmount = info.getAmount() / rate;
        return Currency
                .builder()
                .amount(info.getCurrency()+convertedAmount)
                .countryCurr(countryCurrency)
                .build();
    }

    private TopCities.CityInfo getCityInfoFromCity(CityPopulation city) {
        return TopCities.CityInfo.builder()
                .name(city.getCity())
                .year(city.getPopulationCounts().get(0).getYear())
                .population(city.getPopulationCounts().get(0).getPopulation())
                .build();
    }

    private void sortCitiesByPopulation(List<CityPopulation> population) {
        population
                .sort((city1, city2) -> {
                    double population1 = Double.parseDouble(city1.getPopulationCounts().get(0).getPopulation());
                    double population2 = Double.parseDouble(city2.getPopulationCounts().get(0).getPopulation());
                    return Double.compare(population2, population1);
                });

    }
}
