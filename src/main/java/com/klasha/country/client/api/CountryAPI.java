package com.klasha.country.client.api;

import com.klasha.country.client.config.FeignClientConfig;
import com.klasha.country.dtos.global.CountryAPIResponse;
import com.klasha.country.dtos.request.CityRequest;
import com.klasha.country.dtos.request.CountryRequest;
import com.klasha.country.dtos.response.*;
import com.klasha.country.route.CountryAPIRoutes;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(
        name = "country",
        url = "${country.api.baseurl}",
        configuration = FeignClientConfig.class
)
public interface CountryAPI {

    @RequestMapping(
            method = RequestMethod.POST,
            path = CountryAPIRoutes.LOCATION,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<CountryAPIResponse<CountryLocation>> getCountryLocation(@RequestBody CountryRequest request);

    @RequestMapping(
            method = RequestMethod.POST,
            path = CountryAPIRoutes.CAPITAL_CITY,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<CountryAPIResponse<CountryCapital>> getCountryCapital(@RequestBody CountryRequest request);

    @RequestMapping(
            method = RequestMethod.POST,
            path = CountryAPIRoutes.POPULATION,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<CountryAPIResponse<CountryPopulation>> getCountryPopulation(@RequestBody CountryRequest request);

    @RequestMapping(
            method = RequestMethod.POST,
            path = CountryAPIRoutes.CURRENCY,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<CountryAPIResponse<CountryCurrency>> getCountryCurrency(@RequestBody CountryRequest request);

    @RequestMapping(
            method = RequestMethod.POST,
            path = CountryAPIRoutes.STATES,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<CountryAPIResponse<StateResponse>> getCountryStates(CountryRequest countryRequest);

    @RequestMapping(
            method = RequestMethod.POST,
            path = CountryAPIRoutes.CITIES,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<CountryAPIResponse<List<String>>> getStateCities(CityRequest countryRequest);

    @RequestMapping(
            method = RequestMethod.POST,
            path = CountryAPIRoutes.CITIES_POPULATION,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<CountryAPIResponse<List<CityPopulation>>> getCitiesPopulation(CountryRequest countryRequest);
}
