package com.example.demo.prompt.controller;

import com.example.demo.prompt.model.UserInput;
import lombok.AllArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import static org.springframework.ai.chat.memory.ChatMemory.CONVERSATION_ID;

@RestController
@RequestMapping("/api")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ChatController {

    /**
     * Flow:
     * USER -> CHAT CLIENT -> ADVISORS -> LLM CHAT MODEL -> RESPONSE -> ADVISORS -> USER
     *
     */

    private final ChatClient chatClient;
    @Qualifier("chatMemoryClient")
    private final ChatClient chatMemoryClient;

    @Value("classpath:/templates/prompt/injectionDetectionTemplate.st")
    Resource injectionDetectionTemplate;
    @Value("classpath:/templates/prompt/summaryPromptTemplate.st")
    Resource summaryPromptTemplate;

    /**
     * Simple GPT Wrapper
     *
     */
    @GetMapping("/chat")
    public String chat(@RequestParam("message") String message) {
        return chatClient.prompt(message).call().content();
    }

    /**
     * Simple GPT Wrapper using Reactive Stream
     */
    @GetMapping("/stream")
    public Flux<String> stream(@RequestParam("message") String message) {
        return chatClient.prompt().user(message).stream().content();
    }

    @GetMapping("/chat-memory")
    public ResponseEntity<String> chatMemory(@RequestHeader("userId") String userId,
                                             @RequestParam("message") String message) {
        return ResponseEntity.ok(chatMemoryClient.prompt().user(message).advisors(
                        advisorSpec -> advisorSpec.param(CONVERSATION_ID, userId)
                )
                .call().content());
    }

    /**
     * Simple Prompt Injection
     * */
    @GetMapping("/chat/inject")
    public ResponseEntity<String> inject(@RequestBody UserInput input) {
        var response = chatClient
                .prompt()
                .user(promptUserSpec ->
                        promptUserSpec.text(injectionDetectionTemplate)
                                .param("input", input.getPrompt()))
                .call().content();
        String value;
        try {
            value = switch (response != null ? response.toLowerCase() : null) {
                case "unsafe" -> throw new IllegalArgumentException("Potential prompt injection detected");
                case "safe" -> chatMemoryClient.prompt()
                        .user(promptUserSpec ->
                                promptUserSpec.text(summaryPromptTemplate)
                                        .param("input", input.getPrompt()))
                        .call().content();
                case null -> throw new IllegalArgumentException("Got a null response from LLM");
                default -> throw new IllegalArgumentException("Invalid response");
            };
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok(value);
    }
}
