package com.mediconnect.patientservice.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientRequest {

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @Email(message = "Invalid email")
    @NotBlank(message = "Email is required")
    private String email;

    private String phone;
    private LocalDate birthDate;
    private String address;
    private String bloodType;
    private String insuranceId;
    private List<String> allergies;
    private String keycloakId;
}