package com.mediconnect.aiservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class OllamaService {

    @Value("${ollama.base-url:http://localhost:11434}")
    private String ollamaBaseUrl;

    @Value("${ollama.model:llama3.2:1b}")
    private String ollamaModel;

    private final RestTemplate restTemplate;

    public String chat(String userMessage, String systemPrompt) {
        try {
            String url = ollamaBaseUrl + "/api/chat";

            Map<String, Object> request = new HashMap<>();
            request.put("model", ollamaModel);
            request.put("stream", false);

            List<Map<String, String>> messages = new ArrayList<>();

            if (systemPrompt != null && !systemPrompt.isEmpty()) {
                Map<String, String> system = new HashMap<>();
                system.put("role", "system");
                system.put("content", systemPrompt);
                messages.add(system);
            }

            Map<String, String> user = new HashMap<>();
            user.put("role", "user");
            user.put("content", userMessage);
            messages.add(user);

            request.put("messages", messages);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

            ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Map<String, Object> body = response.getBody();
                Map<String, Object> message = (Map<String, Object>) body.get("message");
                if (message != null) {
                    return (String) message.get("content");
                }
            }
            return "Je suis désolé, je ne peux pas répondre pour le moment.";

        } catch (Exception e) {
            log.error("❌ Ollama error: {}", e.getMessage());
            return "Service IA temporairement indisponible. Veuillez réessayer.";
        }
    }
}