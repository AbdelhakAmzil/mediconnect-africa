package com.mediconnect.doctorservice.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorRequest {

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @Email(message = "Invalid email")
    @NotBlank(message = "Email is required")
    private String email;

    private String phone;

    @NotBlank(message = "Specialization is required")
    private String specialization;

    private String licenseNumber;
    private String hospital;
    private String city;
    private String country;
    private int yearsOfExperience;
    private List<String> languages;
    private String keycloakId;
}