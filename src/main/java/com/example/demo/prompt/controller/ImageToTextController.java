package com.example.demo.prompt.controller;

import lombok.AllArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ImageToTextController {
    @Qualifier("geminiChatClient")
    private final ChatClient chatClient;

    @GetMapping("explain")
    public ResponseEntity<String> explainImageToText() {
        String response = chatClient.prompt()
                .user(u -> u.text("Explain what do you see on this picture?")
                        .media(MimeTypeUtils.IMAGE_JPEG, new ClassPathResource("static/cat.jpg")))
                .call()
                .content();
        return ResponseEntity.ok(response);
    }
}
