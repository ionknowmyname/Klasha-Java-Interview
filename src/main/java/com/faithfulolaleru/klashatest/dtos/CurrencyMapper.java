package com.faithfulolaleru.klashatest.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyMapper {

    private String sourceCurrency;
    private String targetCurrency;
    private double rate;
}
