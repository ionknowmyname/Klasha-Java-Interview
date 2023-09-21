package com.faithfulolaleru.klashatest.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConverterResponse {

    private String countryCurrency;
    private Double convertedAmount;
}
