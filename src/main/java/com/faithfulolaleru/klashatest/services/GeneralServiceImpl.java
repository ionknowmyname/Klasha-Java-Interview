package com.faithfulolaleru.klashatest.services;

import com.faithfulolaleru.klashatest.clients.GeneralClient;
import com.faithfulolaleru.klashatest.dtos.*;
import com.faithfulolaleru.klashatest.exception.ErrorResponse;
import com.faithfulolaleru.klashatest.exception.GeneralException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class GeneralServiceImpl implements GeneralService {

    private final GeneralClient generalClient;

    private ObjectMapper objectMapper = new ObjectMapper();

    private final List<CurrencyMapper> conversionRates = new ArrayList<>();


    @Override
    public List<CityPopulationResponse> getMostPopulatedCities(Integer N) {

        CityPopulationRequest forItaly = createCityPopulationRequest("italy", N);
        CityPopulationRequest forNewZealand = createCityPopulationRequest("new zealand", N);
        CityPopulationRequest forGhana = createCityPopulationRequest("ghana", N);

        JsonNode italyCitiesPopulation, newZealandCitiesPopulation, ghanaCitiesPopulation;

        try {
            italyCitiesPopulation = generalClient.getPopulationPerCityInCountry(forItaly);
            newZealandCitiesPopulation = generalClient.getPopulationPerCityInCountry(forNewZealand);
            ghanaCitiesPopulation = generalClient.getPopulationPerCityInCountry(forGhana);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new GeneralException(HttpStatus.BAD_REQUEST, ErrorResponse.ERROR_FEIGN,
                    "Error, Try again");
        }

        // log.info("italyCitiesPopulation --> ", italyCitiesPopulation);

        List<CityPopulationResponse> response1 = buildResponse(italyCitiesPopulation);
        List<CityPopulationResponse> response2 = buildResponse(newZealandCitiesPopulation);
        List<CityPopulationResponse> response3 = buildResponse(ghanaCitiesPopulation);


        List<CityPopulationResponse> toReturn = Stream.of(response1, response2, response3)
                                                    .flatMap(List::stream)
                                                    .collect(Collectors.toList());


        Collections.sort(toReturn, (m1, m2) -> m2.getPopulation().compareTo(m1.getPopulation()));

        return toReturn.stream().limit(N).collect(Collectors.toList());
    }

    @Override
    public CountryResponse getCountryDetails(String country) {

        JsonNode capitalData, populationData, locationData, currencyData;

        CountryRequest request = new CountryRequest(country);

        try {
            capitalData = generalClient.getCountryCapital(request);
            populationData = generalClient.getCountryPopulation(request);
            locationData = generalClient.getCountryLocation(request);
            currencyData = generalClient.getCountryCurrency(request);

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new GeneralException(HttpStatus.BAD_REQUEST, ErrorResponse.ERROR_FEIGN,
                    "Error, Try again");
        }

        Map<String, String> capital = (Map<String, String>) buildResponse2("capital", capitalData);
        Integer population = (Integer) buildResponse2("population", populationData);
        Map<String, Integer> location = (Map<String, Integer>) buildResponse2("location", locationData);
        String currency = (String) buildResponse2("currency", currencyData);


        return CountryResponse.builder()
                .capitalCity(capital.get("capital"))
                .population(population)
                .location(location)
                .currency(currency)
                .iso2(capital.get("iso2"))
                .iso3(capital.get("iso3"))
                .build();
    }

    @Override
    public List<StateResponse> getCitiesAndStatesInCountry(String country) {

        JsonNode statesData;

        CountryRequest request = new CountryRequest(country);

        try {
            statesData = generalClient.getCitiesAndStatesInCountry(request);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new GeneralException(HttpStatus.BAD_REQUEST, ErrorResponse.ERROR_FEIGN,
                    "Error, Try again");
        }

        List<StateResponse> stateResponses = buildResponse3(statesData);

        return  stateResponses;
    }

    @Override
    public ConverterResponse converter(String country, Double amount, String targetCurrency) {

        JsonNode currencyData;

        CountryRequest request = new CountryRequest(country);

        try {
            currencyData = generalClient.getCountryCurrency(request);

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new GeneralException(HttpStatus.BAD_REQUEST, ErrorResponse.ERROR_FEIGN,
                    "Error, Try again");
        }

        String countryCurrency = currencyData.get("data").get("currency").textValue();


        // readCsvFile();  //  load csv to list
        readCsvFile2();

        // convert from countryCurrency to targetCurrency

        Optional<CurrencyMapper> foundConversion = conversionRates.stream()
                .filter(currencyMapper -> currencyMapper.getSourceCurrency().equalsIgnoreCase(countryCurrency))
                .findFirst();

        Double result = 0.0;
        String targetCurrency1 = foundConversion.get().getTargetCurrency();
        if(foundConversion.isPresent() && targetCurrency1.equalsIgnoreCase(targetCurrency)) {
            double rate = foundConversion.get().getRate();
            result  = rate * amount;
        }

        return new ConverterResponse(countryCurrency, result);
    }



    /*public List<CityPopulationResponse> buildResponse(JsonNode input, Integer N) {

        List<CityPopulationResponse> toReturn = new ArrayList<>();

        if (input.has("data") && input.get("data").isArray()) {

            JsonNode populationArray = input.get("data");

            for (JsonNode single : populationArray) {
                String city = single.get("city").textValue();
                Integer population = Integer.valueOf(single.get("populationCounts").get(0).get("value").textValue());

                CityPopulationResponse response = new CityPopulationResponse(city, population);
                toReturn.add(response);
            }
        }

        Collections.sort(toReturn);

        return toReturn.stream().limit(N).collect(Collectors.toList());
    }*/

    public List<CityPopulationResponse> buildResponse(JsonNode input) {

        List<CityPopulationResponse> toReturn = new ArrayList<>();

        if (input.has("data") && input.get("data").isArray()) {

            JsonNode populationArray = input.get("data");

            for (JsonNode single : populationArray) {
                String city = single.get("city").textValue();
                Integer population = single.get("populationCounts").get(0).get("value").intValue();

                CityPopulationResponse response = new CityPopulationResponse(city, population);
                toReturn.add(response);
            }
        }

        return toReturn;
    }

    public Object buildResponse2(String key, JsonNode input) {

        Object toReturn;

        switch(key) {
            case "capital":
                Map<String, String> map = new HashMap<>();
                map.put("capital", input.get("data").get("capital").textValue());
                map.put("iso2", input.get("data").get("iso2").textValue());
                map.put("iso3", input.get("data").get("iso3").textValue());

                return map;
            case "population":
                return input.get("data").get("populationCounts").get(0).get("value").intValue();
            case "location":
                Map<String, Integer> map2 = new HashMap<>();
                map2.put("long", input.get("data").get("long").intValue());
                map2.put("lat", input.get("data").get("lat").intValue());

                return map2;
            case "currency":
                return input.get("data").get("currency").textValue();
            default:
                throw new GeneralException(HttpStatus.BAD_REQUEST, ErrorResponse.ERROR_GENERAL,
                        "Invalid Key");
        }


    }

    public List<StateResponse> buildResponse3(JsonNode input) {

        List<StateResponse> toReturn = new ArrayList<>();
        List<String> citiesInState = new ArrayList<>();
        String stateName = "";

        if (input.has("data") && input.get("data").get("states").isArray()) {

            String countryName = input.get("data").get("name").textValue();
            JsonNode statesArray = input.get("data").get("states");
            JsonNode cityData;

            for (JsonNode singleState : statesArray) {

                stateName = singleState.get("name").textValue();
                CityRequest request = createCityRequest(countryName, stateName);
                cityData = generalClient.getCitiesInState(request).get("data");

                try {
                    citiesInState = jsonNodeToList(cityData);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    throw new GeneralException(HttpStatus.BAD_REQUEST, ErrorResponse.ERROR_FEIGN,
                            "Error, Try again");
                }

                StateResponse response = new StateResponse(stateName, citiesInState);

                toReturn.add(response);
            }


        }

        return toReturn;
    }



    public CityPopulationRequest createCityPopulationRequest(String country, Integer N) {

        return CityPopulationRequest.builder()
                .order("dsc")
                .orderBy("population")
                .limit(N)
                .country(country)
                .build();
    }

    public CityRequest createCityRequest(String country, String state) {

        return CityRequest.builder()
                .country(country)
                .state(state)
                .build();
    }

    private List<String> jsonNodeToList(JsonNode input) throws JsonProcessingException {

        List<String> cities = new ArrayList<>();

        for (JsonNode city: input) {
            String s = objectMapper.treeToValue(city, String.class);
            cities.add(s);
        }

        return cities;
    }

    private void readCsvFile() {

        try {
            String filePath = "classpath:exchange_rate.csv"; // Path to your CSV file
            BufferedReader br = new BufferedReader(new FileReader(ResourceUtils.getFile(filePath)));
            String line;

            // Skip the header line
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] data = StringUtils.split(line, ",");
                log.info("data.length --> ", data.length);
                if (data.length == 3) {
                    CurrencyMapper mapper = new CurrencyMapper();
                    mapper.setSourceCurrency(data[0]);
                    mapper.setTargetCurrency(data[1]);
                    mapper.setRate(Double.parseDouble(data[2]));

                    conversionRates.add(mapper);
                }
            }
            br.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new GeneralException(HttpStatus.BAD_REQUEST, ErrorResponse.ERROR_GENERAL,
                    "Error, Try again");

        }
    }

    public void readCsvFile2() {

        File file = new File(Objects.requireNonNull(getClass().getClassLoader().getResource("exchange_rate.csv")).getFile());

        InputStream is = null;
        try {
            is = new FileInputStream(file);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            throw new GeneralException(HttpStatus.BAD_REQUEST, ErrorResponse.ERROR_GENERAL,
                    "File not found");
        }

        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
             CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withHeader())) {

            // CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim()

            log.info("Csv parser header map --> ", csvParser.getHeaderMap().entrySet());

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();


            // quick check that its reading something
            csvRecords.forEach(csvRecord -> System.out.println(csvRecord.get("sourceCurrency")));
            csvRecords.forEach(csvRecord -> System.out.println(csvRecord.getRecordNumber()));

            for (CSVRecord csvRecord : csvRecords) {
                // Integer id = Integer.valueOf(csvRecord.get("Id"));
                String sourceCurrency = csvRecord.get("sourceCurrency");
                String targetCurrency = csvRecord.get("targetCurrency");
                Double rate = Double.parseDouble(csvRecord.get("rate"));


                CurrencyMapper mapper = new CurrencyMapper(sourceCurrency, targetCurrency, rate);
                conversionRates.add(mapper);
            }

        } catch (IOException ex) {
            ex.printStackTrace();
            throw new GeneralException(HttpStatus.BAD_REQUEST, ErrorResponse.ERROR_GENERAL,
                    "Failed to parse csv");
        }
    }
}
