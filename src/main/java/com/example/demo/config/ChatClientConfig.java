package com.example.demo.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.google.genai.GoogleGenAiChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ChatClientConfig {

    @Bean(name = "chatClient")
    public ChatClient chatClient(OpenAiChatModel chatModel,
                                 SimpleLoggerAdvisor perplexityLoggerAdvisor) {
        return ChatClient.builder(chatModel)
                .defaultAdvisors(perplexityLoggerAdvisor)
//                .defaultSystem()
//                .defaultUser()
                .build();
    }


    @Bean(name = "chatMemoryClient")
    public ChatClient chatMemoryClient(OpenAiChatModel chatModel,
                                       MessageChatMemoryAdvisor memoryAdvisor,
                                       SimpleLoggerAdvisor perplexityLoggerAdvisor) {
        return ChatClient.builder(chatModel)
                .defaultAdvisors(List.of(perplexityLoggerAdvisor, memoryAdvisor))
                .build();
    }


    @Bean("geminiChatClient")
    public ChatClient geminiChatClient(GoogleGenAiChatModel chatModel,
                                       MessageChatMemoryAdvisor memoryAdvisor,
                                       SimpleLoggerAdvisor geminiLoggerAdvisor) {
        return ChatClient.builder(chatModel)
                .defaultAdvisors(List.of(geminiLoggerAdvisor, memoryAdvisor))
                .build();
    }

}
