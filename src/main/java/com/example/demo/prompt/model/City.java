package com.example.demo.prompt.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class City {
    private String cityName;
    private double area;
    private double population;
}
