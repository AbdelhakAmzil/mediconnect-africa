package com.mediconnect.consultationservice.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsultationRequest {
    private Long appointmentId;
    private Long patientId;
    private Long doctorId;
}