package com.klasha.country.controller;

import com.klasha.country.dtos.global.ApiResponse;
import com.klasha.country.dtos.request.CurrencyInfo;
import com.klasha.country.service.CountryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Get Country Information")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Get country info",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class))})
    }
    )
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
                        .time(String.valueOf(timeElapsed.toMillis() / 1000.0))
                        .data(data)
                        .build());
    }

    @Operation(summary = "Get Cities and states in a country")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Cities and states",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class))})
    }
    )
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
                        .time(String.valueOf(timeElapsed.toMillis() / 1000.0))
                        .data(data)
                        .build());
    }

    @Operation(summary = "Get Sorted cities in a country")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",
                    description = "Get cities in a country ordered by population",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class))})
    }
    )
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
                        .time(String.valueOf(timeElapsed.toMillis() / 1000.0))
                        .data(topCities)
                        .build());
    }

    @Operation(summary = "Convert currencies")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "convert money from one form to another",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class))})
    }
    )

    @PostMapping("convert")
    public ResponseEntity<ApiResponse<?>> convertCurrency(
            @RequestParam String country,
            @Valid @RequestBody CurrencyInfo info
    ) {
        Instant start = Instant.now();
        var conversion = countryService.convertCurrency(country, info);
        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end);
        return ResponseEntity.ok()
                .body(ApiResponse.builder()
                        .error(false)
                        .msg(EXCHANGE_MESSAGE)
                        .time(String.valueOf(timeElapsed.toMillis() / 1000.0))
                        .data(conversion)
                        .build());
    }
}
