package com.faithfulolaleru.klashatest.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CityPopulationResponse  {  // implements Comparable<CityPopulationResponse>

    private String city;
    private Integer population;

//    @Override
//    public int compareTo(@NotNull CityPopulationResponse o) {
//        return Integer.compare(o.population, this.population);
//    }
}
