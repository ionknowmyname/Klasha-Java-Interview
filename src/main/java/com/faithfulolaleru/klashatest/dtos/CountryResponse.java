package com.faithfulolaleru.klashatest.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CountryResponse {

    private Integer population;
    private String capitalCity;
    private Map<String, Integer> location;   // for longitude & latitude
    private String currency;
    private String iso2;
    private String iso3;

}
