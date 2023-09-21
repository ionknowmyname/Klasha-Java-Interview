package com.faithfulolaleru.klashatest.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CityPopulationRequest {

    private Integer limit;  // when you remove limit, it returns everything
    private String order;
    private String orderBy;
    private String country;
}
