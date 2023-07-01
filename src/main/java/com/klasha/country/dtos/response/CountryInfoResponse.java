package com.klasha.country.dtos.response;

import com.klasha.country.dtos.global.ISO;
import com.klasha.country.dtos.global.Location;
import lombok.*;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CountryInfoResponse {

    private double population;
    private String capitalCity;
    private Location location;
    private String currency;
    private ISO iso;

}
