package com.example.demo.prompt.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class State {
    private String stateName;
    private String capital;
    private List<City> cities;
}
