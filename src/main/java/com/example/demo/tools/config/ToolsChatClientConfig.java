package com.example.demo.tools.config;

import com.example.demo.tools.helper.HelpDeskTools;
import com.example.demo.tools.helper.TimeTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.google.genai.GoogleGenAiChatModel;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.execution.DefaultToolExecutionExceptionProcessor;
import org.springframework.ai.tool.execution.ToolExecutionExceptionProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ToolsChatClientConfig {

    @Bean("timeChatClient")
    public ChatClient chatClient(GoogleGenAiChatModel chatModel,
                                 MessageChatMemoryAdvisor memoryAdvisor,
                                 SimpleLoggerAdvisor loggerAdvisor,
                                 TimeTools timeTools) {

        return ChatClient.builder(chatModel)
                .defaultTools(timeTools)    // This is default tools, Tools can also be sent in the prompt request as well
                .defaultAdvisors(List.of(loggerAdvisor, memoryAdvisor))
                .build();
    }

    /**
     * Tools Exceptions directly thrown to application, instead of sending it to LLM
     *
     */
    @Bean
    ToolExecutionExceptionProcessor toolExecutionExceptionProcessor() {
        return new DefaultToolExecutionExceptionProcessor(true);
    }

    @Bean
    List<ToolCallback> toolCallbacks(HelpDeskTools helpDeskTools) {
        return List.of(ToolCallbacks.from(helpDeskTools));
    }
}
