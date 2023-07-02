package com.klasha.country.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CityRequest {

    @NotBlank(message = "Country should be present")
    private String country;

    @NotBlank(message = "state should be present")
    private String state;

}
