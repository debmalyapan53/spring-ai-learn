package com.example.demo.prompt.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HRAssistantController {

    /**
     * Message Roles:
     * System -> Instructions how LLM should behave
     * User -> Input
     * Assistant -> LLM Response
     * Function (OpenAI) -> Special instruction to fetch data or run method etc
     * */

    @Autowired
    private ChatClient chatClient;

    @Value("classpath:/templates/prompt/systemPromptTemplate.st")
    Resource systemPromptTemplate;

    /**
     * Prompt stuffing using system templates
     * Precursor to RAGs
     * */
    @GetMapping("/hr-help")
    public String promptStuffing(@RequestParam("message") String message) {
        return chatClient
                .prompt()
                .system(systemPromptTemplate)
                .user(message)
                .call().content();
    }

}