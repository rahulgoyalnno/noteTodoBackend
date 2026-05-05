package com.smartnotes.smartnotes_backend.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Slf4j
@Service
public class AiService {

    private final RestClient restClient;
    private final String model;

    public AiService(
            @Value("${ollama.base-url}") String baseUrl,
            @Value("${ollama.model}")    String model) {
        this.model = model;
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public String summarise(String title, String content) {
        String prompt = """
            Summarise this note in 1-2 sentences. Be concise and clear.
            
            Title: %s
            Content: %s
            
            Summary:
            """.formatted(title, content);

        return callOllama(prompt);
    }

    public String suggestFollowUp(String title, String content) {
        String prompt = """
            Based on this note, suggest ONE specific follow-up action 
            the user should take. Start with a verb. Max 15 words.
            
            Title: %s
            Content: %s
            
            Follow-up action:
            """.formatted(title, content);

        return callOllama(prompt);
    }

    private String callOllama(String prompt) {
        try {
            Map<?, ?> response = restClient.post()
                    .uri("/api/generate")
                    .body(Map.of(
                            "model",  "llama3.2",
                            "prompt", prompt,
                            "stream", false          // wait for full response
                    ))
                    .retrieve()
                    .body(Map.class);

            return response != null
                    ? response.get("response").toString().trim()
                    : "Unable to generate summary";

        } catch (Exception e) {
            log.error("Ollama call failed: {}", e.getMessage());
            return null;   // null = AI unavailable, note still saves fine
        }
    }
}