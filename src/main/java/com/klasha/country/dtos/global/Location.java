package com.klasha.country.dtos.global;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Location {
    private double lat;
    private double log;
}
