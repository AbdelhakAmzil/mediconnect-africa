package com.mediconnect.consultationservice.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "consultations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Consultation {

    @Id
    private String id;

    private Long appointmentId;
    private Long patientId;
    private Long doctorId;

    private String roomToken;

    @org.springframework.data.annotation.Transient
    private ConsultationStatus status;

    private String diagnosis;
    private String prescription;
    private String notes;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private LocalDateTime createdAt;

    public enum ConsultationStatus {
        SCHEDULED, ACTIVE, COMPLETED, CANCELLED
    }
}