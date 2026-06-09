package com.mediconnect.aiservice.service;

import com.mediconnect.aiservice.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AiChatService {

    private final OllamaService ollamaService;
    private final RedisConversationService redisConversationService;

    private static final String SYSTEM_PROMPT =
            "Tu es MediBot, un assistant médical bienveillant de MediConnect Africa. " +
                    "Tu aides les patients avec des informations médicales générales, " +
                    "des conseils de santé et des orientations vers des spécialistes. " +
                    "Tu ne poses pas de diagnostic. Tu réponds toujours en français. " +
                    "En cas d'urgence, tu recommandes immédiatement d'appeler le 15 ou de se rendre aux urgences.";

    public AiResponse chat(AiRequest request) {
        String sessionId = request.getSessionId() != null
                ? request.getSessionId()
                : UUID.randomUUID().toString();

        log.info("🤖 AI chat request - session: {}, patient: {}",
                sessionId, request.getPatientId());

        // Sauvegarder le message utilisateur
        redisConversationService.saveMessage(sessionId, "user", request.getMessage());

        // Appel Ollama
        String aiResponseText = ollamaService.chat(request.getMessage(), SYSTEM_PROMPT);

        // Sauvegarder la réponse AI
        redisConversationService.saveMessage(sessionId, "assistant", aiResponseText);

        log.info("✅ AI response generated for session: {}", sessionId);

        return AiResponse.builder()
                .sessionId(sessionId)
                .userMessage(request.getMessage())
                .aiResponse(aiResponseText)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public void clearSession(String sessionId) {
        redisConversationService.clearSession(sessionId);
    }
}