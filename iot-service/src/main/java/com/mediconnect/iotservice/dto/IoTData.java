package com.mediconnect.iotservice.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IoTData {
    private Long patientId;
    private String deviceId;
    private String deviceType;   // HEART_RATE, BLOOD_PRESSURE, TEMPERATURE, SPO2
    private Double value;
    private String unit;
    private Long timestamp;
    private boolean alert;
}