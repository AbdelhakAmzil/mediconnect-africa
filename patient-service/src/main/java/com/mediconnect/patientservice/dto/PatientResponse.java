package com.mediconnect.patientservice.dto;

import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private LocalDate birthDate;
    private String address;
    private String bloodType;
    private String insuranceId;
    private List<String> allergies;
    private LocalDateTime createdAt;
}