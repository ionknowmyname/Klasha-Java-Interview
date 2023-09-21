package com.faithfulolaleru.klashatest.controllers;

import com.faithfulolaleru.klashatest.dtos.*;
import com.faithfulolaleru.klashatest.services.GeneralService;
import com.faithfulolaleru.klashatest.utils.AppResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "api/v1")
@AllArgsConstructor
public class GeneralController {

    private final GeneralService generalService;


    @GetMapping("/population")
    public AppResponse<?> getMostPopulatedCities(@RequestParam Integer N) {

        List<CityPopulationResponse> response = generalService.getMostPopulatedCities(N);

        return AppResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .httpStatus(HttpStatus.OK)
                .message("SUCCESSFUL")
                .data(response)
                .build();
    }

    @GetMapping("/country")
    public AppResponse<?> getCountryDetails(@RequestParam String country) {

        CountryResponse response = generalService.getCountryDetails(country);

        return AppResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .httpStatus(HttpStatus.OK)
                .message("SUCCESSFUL")
                .data(response)
                .build();
    }

    @GetMapping("/country/all")
    public AppResponse<?> getCitiesAndStatesInCountry(@RequestParam String country) {

        List<StateResponse> response = generalService.getCitiesAndStatesInCountry(country);

        return AppResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .httpStatus(HttpStatus.OK)
                .message("SUCCESSFUL")
                .data(response)
                .build();
    }

    @GetMapping("/convert")
    public AppResponse<?> converter(@RequestParam String country,
                                    @RequestParam Double amount,
                                    @RequestParam String targetCurrency) {

        ConverterResponse response = generalService.converter(country, amount, targetCurrency);

        return AppResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .httpStatus(HttpStatus.OK)
                .message("SUCCESSFUL")
                .data(response)
                .build();
    }
}
