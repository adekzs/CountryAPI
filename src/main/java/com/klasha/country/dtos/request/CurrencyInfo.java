package com.klasha.country.dtos.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CurrencyInfo {

    @NotBlank(message = "currency should be present")
    private String currency;

    @NotNull(message = "amount should be present")
    private Double amount;
}
