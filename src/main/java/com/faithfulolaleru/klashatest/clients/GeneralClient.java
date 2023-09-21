package com.faithfulolaleru.klashatest.clients;

import com.faithfulolaleru.klashatest.dtos.CityPopulationRequest;
import com.faithfulolaleru.klashatest.dtos.CityRequest;
import com.faithfulolaleru.klashatest.dtos.CountryRequest;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "general-client", url = "${general.base.url}", configuration = GeneralClientConfig.class)
public interface GeneralClient {


    @PostMapping("/countries/population/cities/filter")
    JsonNode getPopulationPerCityInCountry(@RequestBody CityPopulationRequest requestDto);

    @PostMapping("/countries/capital")
    JsonNode getCountryCapital(@RequestBody CountryRequest request);

    @PostMapping("/countries/positions")
    JsonNode getCountryLocation(@RequestBody CountryRequest request);

    @PostMapping("/countries/population")
    JsonNode getCountryPopulation(@RequestBody CountryRequest request);

    @PostMapping("/countries/currency")
    JsonNode getCountryCurrency(@RequestBody CountryRequest request);

    @PostMapping("/countries/states")
    JsonNode getCitiesAndStatesInCountry(@RequestBody CountryRequest request);

    @PostMapping("countries/state/cities")
    JsonNode getCitiesInState(@RequestBody CityRequest request);
}
