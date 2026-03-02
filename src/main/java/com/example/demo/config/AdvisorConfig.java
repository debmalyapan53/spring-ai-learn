package com.example.demo.config;

import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AdvisorConfig {

    @Bean
    public SimpleLoggerAdvisor loggerAdvisor(){
        return new SimpleLoggerAdvisor();
    }

    @Bean
    public MessageChatMemoryAdvisor memoryAdvisor(ChatMemory chatMemory) {
        return MessageChatMemoryAdvisor.builder(chatMemory).build();
    }


    @Bean
    ChatMemory chatMemory(JdbcChatMemoryRepository jdbcChatMemoryRepository) {
        return MessageWindowChatMemory.builder().maxMessages(10)                //default is 20
                .chatMemoryRepository(jdbcChatMemoryRepository).build();
    }
}
