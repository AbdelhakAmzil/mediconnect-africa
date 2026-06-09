package com.mediconnect.aiservice.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AiRequest {
    private String sessionId;
    private Long patientId;
    private String message;
}