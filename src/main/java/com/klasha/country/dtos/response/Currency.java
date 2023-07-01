package com.klasha.country.dtos.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Currency {
    private String countryCurr;
    private String amount;
}
