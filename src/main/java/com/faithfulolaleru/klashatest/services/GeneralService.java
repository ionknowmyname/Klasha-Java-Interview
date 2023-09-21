package com.faithfulolaleru.klashatest.services;

import com.faithfulolaleru.klashatest.dtos.*;

import java.util.List;

public interface GeneralService {

    List<CityPopulationResponse> getMostPopulatedCities(Integer N);

    CountryResponse getCountryDetails(String country);

    List<StateResponse>  getCitiesAndStatesInCountry(String country);

    ConverterResponse converter(String country, Double amount, String targetCurrency);
}
