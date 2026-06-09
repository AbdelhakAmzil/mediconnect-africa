package com.mediconnect.consultationservice.dto;

import com.mediconnect.consultationservice.entity.Consultation;
import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsultationResponse {
    private String id;
    private Long appointmentId;
    private Long patientId;
    private Long doctorId;
    private String roomToken;
    private Consultation.ConsultationStatus status;
    private String diagnosis;
    private String prescription;
    private String notes;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime createdAt;
}