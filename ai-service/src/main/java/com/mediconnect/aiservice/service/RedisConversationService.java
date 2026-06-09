package com.mediconnect.aiservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisConversationService {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String PREFIX = "ai:session:";
    private static final Duration TTL = Duration.ofHours(2);

    public void saveMessage(String sessionId, String role, String content) {
        String key = PREFIX + sessionId;
        Map<String, String> message = new HashMap<>();
        message.put("role", role);
        message.put("content", content);
        message.put("timestamp", String.valueOf(System.currentTimeMillis()));
        redisTemplate.opsForList().rightPush(key, message);
        redisTemplate.expire(key, TTL);
        log.info("💾 Message saved to Redis for session: {}", sessionId);
    }

    public List<Map<String, String>> getHistory(String sessionId) {
        String key = PREFIX + sessionId;
        List<Object> raw = redisTemplate.opsForList().range(key, 0, -1);
        List<Map<String, String>> history = new ArrayList<>();
        if (raw != null) {
            for (Object obj : raw) {
                if (obj instanceof Map) {
                    history.add((Map<String, String>) obj);
                }
            }
        }
        return history;
    }

    public void clearSession(String sessionId) {
        redisTemplate.delete(PREFIX + sessionId);
        log.info("🗑️ Session cleared: {}", sessionId);
    }
}