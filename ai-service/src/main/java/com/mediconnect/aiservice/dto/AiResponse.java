package com.mediconnect.aiservice.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AiResponse {
    private String sessionId;
    private String userMessage;
    private String aiResponse;
    private LocalDateTime timestamp;
}