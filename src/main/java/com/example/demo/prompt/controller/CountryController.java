package com.example.demo.prompt.controller;

import com.example.demo.prompt.model.Country;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class CountryController {

    @Autowired
    private ChatClient chatClient;

    @Value("classpath:/templates/prompt/userPromptTemplate.st")
    Resource userPromptTemplate;

    /**
     * structured output using prompt templates
     *
     */
    @GetMapping("/countries/{countryName}")
    public ResponseEntity<Country> getCountryInfo(@PathVariable("countryName") String countryName) {
        return ResponseEntity.ok(chatClient.prompt()
                        .system("""
                                You are a geography assistant, and helping out a school student for a project.
                                """)
                .user(promptTemplateSpec ->
                        promptTemplateSpec.text(userPromptTemplate)
                                .param("countryName", countryName))
                .call().entity(Country.class));
    }

}
