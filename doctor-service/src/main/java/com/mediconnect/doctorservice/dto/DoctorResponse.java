package com.mediconnect.doctorservice.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String specialization;
    private String licenseNumber;
    private String hospital;
    private String city;
    private String country;
    private double rating;
    private int yearsOfExperience;
    private boolean available;
    private List<String> languages;
    private LocalDateTime createdAt;
}