package com.mediconnect.iotservice.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IoTAlert {
    private Long patientId;
    private String deviceType;
    private Double value;
    private String message;
    private String severity;   // LOW, MEDIUM, HIGH, CRITICAL
    private Long timestamp;
}