package com.klasha.country.controller;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.klasha.country.dtos.global.ApiResponse;
import com.klasha.country.dtos.request.CurrencyInfo;
import com.klasha.country.dtos.response.CityAndStates;
import com.klasha.country.dtos.response.CityAndStates.State;
import com.klasha.country.dtos.response.CountryInfoResponse;
import com.klasha.country.dtos.response.Currency;
import com.klasha.country.dtos.response.TopCities;
import com.klasha.country.dtos.response.TopCities.City;
import com.klasha.country.service.CountryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@WebMvcTest(CountryController.class)
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CountryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CountryService countryService;

    @Test
    void testGetCountryInfoWhenACountryIsPassed() throws Exception {
        CountryInfoResponse nigeriaResponse = CountryInfoResponse.builder().build();
        when(countryService.getInformation("Nigeria")).thenReturn(nigeriaResponse);

        MvcResult result = mockMvc.perform(get("/country?country=Nigeria")).andExpect(status().isOk())
                .andReturn();
        var responseBody = result.getResponse().getContentAsString();
        ApiResponse<CountryInfoResponse> response = new ObjectMapper().readValue(responseBody, getType(ApiResponse.class,
                CountryInfoResponse.class));

        assertFalse(response.isError(), "Wrong error");
        assertNotNull(response.getMsg(), "Message should not be null");
        assertNotNull(response.getData(), "Country Info should be present");

    }
    @Test
    void testGetCityAndState() throws Exception {
        var states = new ArrayList<State>();
        var stateCities = Arrays.asList("Hello", "here", "123");
        states.add(new State("Lagos", stateCities));
        CityAndStates cityAndStates = CityAndStates.builder()
                .states(states)
                .build();
        when(countryService.getStatesAndCities("Nigeria")).thenReturn(cityAndStates);

        MvcResult result = mockMvc.perform(get("/country/states?country=Nigeria"))
                .andExpect(status().isOk())
                .andReturn();
        var responseBody = result.getResponse().getContentAsString();
        ApiResponse<CityAndStates> response = new ObjectMapper().readValue(responseBody, getType(ApiResponse.class,
                CityAndStates.class));

        assertFalse(response.isError(), "Wrong error");
        assertNotNull(response.getMsg(), "Message should not be null");
        assertNotNull(response.getData(), " City and states Info should be present");

    }

    @Test
    void getTopCities() throws Exception {
        TopCities cities = TopCities.builder()
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
        when(countryService.getTopCities(3)).thenReturn(cities);

        MvcResult result = mockMvc.perform(get("/country/population?noOfCities=3"))
                .andExpect(status().isOk())
                .andReturn();

        var responseBody = result.getResponse().getContentAsString();
        ApiResponse<TopCities> response = new ObjectMapper().readValue(responseBody, getType(ApiResponse.class,
                TopCities.class));

        assertFalse(response.isError(), "Wrong error");
        assertNotNull(response.getMsg(), "Message should not be null");
        assertNotNull(response.getData(), " City and states Info should be present");

    }
    @Test
    void convertCurrency() throws Exception {
        CurrencyInfo currencyInfo = CurrencyInfo.builder()
                .currency("JPY")
                .amount(1000.0)
                .build();
        String currencyInfo2 = "{\n" +
                "  \"currency\": \"JPY\",\n" +
                "  \"amount\": 1000\n" +
                "}";
        Currency currency = Currency.builder()
                .countryCurr("NGN")
                .amount("JPY2500")
                .build();
        when(countryService.convertCurrency("Nigeria", currencyInfo))
                .thenReturn(currency);

        MvcResult result = mockMvc.perform(post("/country/convert?country=Nigeria")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(currencyInfo2))
                .andExpect(status().isOk())
                .andReturn();
        var responseBody = result.getResponse().getContentAsString();
        ApiResponse<Currency> response = new ObjectMapper().readValue(responseBody, getType(ApiResponse.class,
                Currency.class));

        assertFalse(response.isError(), "Wrong error");
        assertNotNull(response.getMsg(), "Message should not be null");
        assertNotNull(response.getData(), "Data should not be null");

    }
    private <T, U> JavaType getType(Class<T> type1, Class<U> type2) {
        TypeFactory typeFactory = objectMapper.getTypeFactory();
        return typeFactory.constructParametricType(type1, type2);
    }
}