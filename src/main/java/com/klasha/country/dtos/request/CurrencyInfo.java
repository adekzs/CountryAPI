package com.klasha.country.dtos.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyInfo {

    @NotBlank(message = "currency should be present")
    private String currency;

    @NotNull(message = "amount should be present")
    private Double amount;


}
