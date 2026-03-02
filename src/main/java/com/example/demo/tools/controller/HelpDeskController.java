package com.example.demo.tools.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

import static org.springframework.ai.chat.memory.ChatMemory.CONVERSATION_ID;

/**
 * LLM + Tools = AI Agent
 * Precursor to MCP
 * */
@RestController
@RequestMapping("/api/tools")
public class HelpDeskController {

    /**
     * Default Tools Call Implementation : DefaultToolCallingManager.java
     * Default Tools Converter : DefaultToolCallResultConverter.java
     * */

    private final ChatClient chatClient;
    private final List<ToolCallback> toolCallbacks;
    private final Resource helpDeskSystemTemplate;

    public HelpDeskController(@Qualifier("geminiChatClient") ChatClient chatClient,
                              List<ToolCallback> toolCallbacks,
                              @Value("classpath:templates/prompt/helpDeskSystemPromptTemplate.st")
                              Resource helpDeskSystemTemplate) {
        this.chatClient = chatClient;
        this.toolCallbacks = toolCallbacks;
        this.helpDeskSystemTemplate = helpDeskSystemTemplate;
    }

    /**
     * Flow:
     * Controller -----(USER) context + tools details-----------------------------------> LLM Model
     * Controller <---------(ASSISTANT) I don't know, please invoke this tool method ---- LLM Model
     * Controller <--------------invokes and gets response----------------> Tools
     * Controller -----sends context and tools response---------------------------------> LLM Model (optional)
     * Controller <---------------------(ASSISTANT) final answer ------------------------ LLM Model
     * */
    @GetMapping("/help-desk")
    public ResponseEntity<String> helpDesk(@RequestHeader("userId") String userId,
                                           @RequestParam("message") String message) {
        String answer = chatClient.prompt()
                .advisors(a -> a.param(CONVERSATION_ID, userId))
                .system(helpDeskSystemTemplate)
                .user(message)
                .toolCallbacks(toolCallbacks)
                .toolContext(Map.of("userId", userId))
                .call().content();
        return ResponseEntity.ok(answer);
    }
}
