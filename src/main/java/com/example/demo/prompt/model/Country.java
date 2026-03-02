package com.example.demo.prompt.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Country {
    private String countryName;
    private String capital;
    private List<State> states;
}
