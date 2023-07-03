package com.klasha.country.controller;

import com.klasha.country.dtos.global.ApiResponse;
import com.klasha.country.dtos.request.CurrencyInfo;
import com.klasha.country.service.CountryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.Instant;

import static com.klasha.country.constants.Constants.ResponseMessage.*;

@RestController
@RequestMapping("country")
@RequiredArgsConstructor
public class CountryController {

    private final CountryService countryService;
    @GetMapping
    public ResponseEntity<?> getCountryInfo(@RequestParam String country) {
        Instant start = Instant.now();
        var data = countryService.getInformation(country);
        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end);
        return ResponseEntity.ok()
                .body(ApiResponse.builder()
                        .error(false)
                        .msg(COUNTRY_INFO_SUCCESS)
                        .time(String.valueOf(timeElapsed.toMillis()/1000.0))
                        .data(data)
                        .build());
    }

    @GetMapping("states")
    public ResponseEntity<ApiResponse<?>> getStatesAndCities(@RequestParam String country) {
        Instant start = Instant.now();
        var data = countryService.getStatesAndCities(country);
        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end);
        return ResponseEntity.ok()
                .body(ApiResponse.builder()
                        .error(false)
                        .msg(COUNTRY_AND_STATES_SUCCESS)
                        .time(String.valueOf(timeElapsed.toMillis()/1000.0))
                        .data(data)
                        .build());
    }

    @GetMapping("population")
    public ResponseEntity<ApiResponse<?>> getTopCities(@RequestParam int noOfCities) {
        Instant start = Instant.now();
        var topCities = countryService.getTopCities(noOfCities);
        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end);
        return ResponseEntity.ok()
                .body(ApiResponse.builder()
                        .error(false)
                        .msg(POPULATION_SUCCESS)
                        .time(String.valueOf(timeElapsed.toMillis()/1000.0))
                        .data(topCities)
                        .build());
    }

    @PostMapping("convert")
    public ResponseEntity<ApiResponse<?>> convertCurrency(
            @RequestParam String country,
            @Valid  @RequestBody CurrencyInfo info
             ) {
        Instant start = Instant.now();
        var conversion = countryService.convertCurrency(country, info);
        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end);
        return ResponseEntity.ok()
                .body(ApiResponse.builder()
                        .error(false)
                        .msg(EXCHANGE_MESSAGE)
                        .time(String.valueOf(timeElapsed.toMillis()/1000.0))
                        .data(conversion)
                        .build());
    }
}
