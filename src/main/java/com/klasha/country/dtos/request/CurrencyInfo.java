package com.klasha.country.dtos.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CurrencyInfo {
    private String currency;
    private double amount;
}
