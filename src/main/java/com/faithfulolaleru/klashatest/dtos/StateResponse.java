package com.faithfulolaleru.klashatest.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StateResponse {

    private String stateName;
    private List<String> cities;
}
