package com.klasha.country.service.impl;

import com.klasha.country.dtos.response.TopCities.City;
import com.klasha.country.exception.CustomException;
import com.klasha.country.exception.NotFoundException;
import com.klasha.country.utils.CurrencyTable;
import com.klasha.country.client.api.CountryAPI;
import com.klasha.country.dtos.global.CountryAPIResponse;
import com.klasha.country.dtos.global.ISO;
import com.klasha.country.dtos.global.Location;
import com.klasha.country.dtos.request.CityRequest;
import com.klasha.country.dtos.request.CountryRequest;
import com.klasha.country.dtos.request.CurrencyInfo;
import com.klasha.country.dtos.response.*;
import com.klasha.country.service.CountryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import static com.klasha.country.constants.Constants.Country.*;

@RequiredArgsConstructor
@Service
@Log4j2
public class CountryServiceImpl implements CountryService {

    private final CountryAPI countryAPI;
    private final TaskExecutor taskExecutor;
    private final CurrencyTable currencyTable;

    @Override
    public CountryInfoResponse getInformation(String country) {

        CountryRequest req = new CountryRequest();
        req.setCountry(country);
        CompletableFuture<CountryLocation> countryLocation =
                CompletableFuture.supplyAsync(() -> countryAPI.getCountryLocation(req))
                        .thenApplyAsync(locationResponse -> Objects.requireNonNull(locationResponse.getBody()).getData())
                        .exceptionally(ex -> {
                            throw new NotFoundException("Country not found");
                        });

        CompletableFuture<CountryCapital> capitalCity =
                CompletableFuture.supplyAsync(() -> countryAPI.getCountryCapital(req))
                        .thenApplyAsync(capitalResponse -> Objects.requireNonNull(capitalResponse.getBody()).getData())
                        .exceptionally(ex -> {
                            throw new NotFoundException("Capital Not found");
                        });

        CompletableFuture<CountryPopulation> countryPopulation =
                CompletableFuture.supplyAsync(() -> countryAPI.getCountryPopulation(req))
                        .thenApplyAsync(populationResponse -> Objects.requireNonNull(populationResponse.getBody()).getData())
                        .exceptionally(ex -> {
                            throw new NotFoundException("Population not found");
                        });
        CompletableFuture<CountryCurrency> countryCurrency =
                CompletableFuture.supplyAsync(() -> countryAPI.getCountryCurrency(req))
                        .thenApplyAsync(currencyResponse -> Objects.requireNonNull(currencyResponse.getBody()).getData())
                        .exceptionally(ex -> {
                            throw new NotFoundException("Currency not found");
                        });;

        return CompletableFuture.allOf(countryLocation, countryCurrency, countryPopulation, capitalCity)
                .thenApplyAsync(voidR -> {
                    int size = countryPopulation.join().getPopulationCounts().size();

                    return CountryInfoResponse.builder()
                            .currency(countryCurrency.join().getCurrency())
                            .capitalCity(capitalCity.join().getCapital())
                            .location(Location.builder()
                                    .lat(countryLocation.join().getLat())
                                    .log(countryLocation.join().getLog())
                                            .build())
                            .population(countryPopulation.join().getPopulationCounts().get(size - 1).getValue())
                            .iso(ISO.builder()
                                    .iso2(countryCurrency.join().getIso2())
                                    .iso3(countryCurrency.join().getIso3())
                                    .build())
                            .build();
                }).join();

    }

    @Override
    public CityAndStates getStatesAndCities(String country) {
        CityAndStates cityAndStates = new CityAndStates(new ArrayList<>());
        CountryRequest req = new CountryRequest();
        req.setCountry(country);
        var states = countryAPI.getCountryStates(req);
        List<CompletableFuture<CityAndStates.State>> futures = new ArrayList<>();
        for (var state : Objects.requireNonNull(states.getBody()).getData().getStates()) {
            CompletableFuture<CityAndStates.State> stateWithCities =
                    CompletableFuture.supplyAsync(() -> countryAPI.getStateCities(CityRequest.builder()
                                    .country(country)
                                    .state(state.getName())
                                    .build()),taskExecutor)
                            .thenApplyAsync(t -> {
                                var resp = Objects.requireNonNull(t.getBody()).getData();
                                return CityAndStates.State.builder()
                                        .cities(resp)
                                        .state(state.getName())
                                        .build();

                            }, taskExecutor)
                            .exceptionally(ex -> {
                                log.info("Error fetching cities for " + state.getName());
                                log.error("Received exception {}, throwing new exception!", ex.getMessage());
                                return CityAndStates.State.builder()
                                        .cities(new ArrayList<String>())
                                        .state(state.getName())
                                        .build();
                            });
            futures.add(stateWithCities);
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApplyAsync(ft -> {
                            futures.forEach(t -> cityAndStates.getStates()
                                    .add(t.join()));
                            return ft;
                        }, taskExecutor)
                .join();

        return cityAndStates;
    }

    @Override
    public TopCities getTopCities(int noOfCities) {

        TopCities response = TopCities.builder()
                .ghana(City
                        .builder()
                        .cities(new ArrayList<>())
                        .build())
                .italy(City
                        .builder()
                        .cities(new ArrayList<>())
                        .build())
                .newZealand(City
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
        CompletableFuture<ResponseEntity<CountryAPIResponse<List<CityPopulation>>>> ghanaCitiesPopulation =
                CompletableFuture.supplyAsync(() -> countryAPI.getCitiesPopulation(ghana), taskExecutor)
                        .thenApplyAsync(x -> {
                            sortCitiesByPopulation(Objects.requireNonNull(x.getBody()).getData());
                            return x;
                        });
        CompletableFuture<ResponseEntity<CountryAPIResponse<List<CityPopulation>>>> italyCitiesPopulation =
                CompletableFuture.supplyAsync(() -> countryAPI.getCitiesPopulation(italy), taskExecutor)
                        .thenApplyAsync(x -> {
                            sortCitiesByPopulation(Objects.requireNonNull(x.getBody()).getData());
                            return x;
                        });
        CompletableFuture<ResponseEntity<CountryAPIResponse<List<CityPopulation>>>> newZealandPopulation =
                CompletableFuture.supplyAsync(() -> countryAPI.getCitiesPopulation(newZealand), taskExecutor)
                        .thenApplyAsync(x -> {
                            sortCitiesByPopulation(Objects.requireNonNull(x.getBody()).getData());
                            return x;
                        });
        return CompletableFuture.allOf(ghanaCitiesPopulation, italyCitiesPopulation, newZealandPopulation)
                .thenApplyAsync(f -> {
                    Objects.requireNonNull(ghanaCitiesPopulation.join().getBody()).getData().stream()
                            .limit(noOfCities)
                            .forEach(city -> response.getGhana().getCities().add(getCityInfoFromCity(city)));
                    Objects.requireNonNull(italyCitiesPopulation.join().getBody()).getData().stream()
                            .limit(noOfCities)
                            .forEach(city -> response.getItaly().getCities().add(getCityInfoFromCity(city)));
                    Objects.requireNonNull(newZealandPopulation.join().getBody()).getData().stream()
                            .limit(noOfCities)
                            .forEach(city -> response.getNewZealand().getCities().add(getCityInfoFromCity(city)));
                    return response;
                }, taskExecutor).join();
    }

    @Override
    public Currency convertCurrency(String country, CurrencyInfo info) {
        var currencyData = currencyTable.getCurrency();
        var currencyInfo = countryAPI.getCountryCurrency(CountryRequest
                .builder()
                .country(country)
                .build());
        String countryCurrency = Objects.requireNonNull(currencyInfo.getBody()).getData().getCurrency();
        String countryCurrency2 = countryCurrency + "-" + info.getCurrency();
        try {
            double rate = 0.0;
            double  convertedAmount = 0.0;
            if (currencyData.get(countryCurrency2) != null) {
                 rate = currencyData.get(countryCurrency2);
                 convertedAmount = info.getAmount() / rate;
            } else {
                 countryCurrency2 = info.getCurrency()+ "-" + countryCurrency;
                 rate = currencyData.get(countryCurrency2);
                 convertedAmount = info.getAmount() * rate;
            }
            DecimalFormat decimalFormat = new DecimalFormat("#0.00");

            // Format the double value
            String formattedNumber = decimalFormat.format(convertedAmount);
            return Currency
                    .builder()
                    .amount(info.getCurrency() + formattedNumber)
                    .countryCurr(countryCurrency)
                    .build();
        } catch (Exception ex) {
            throw new CustomException("Exchange not available", HttpStatus.NOT_FOUND);
        }
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
