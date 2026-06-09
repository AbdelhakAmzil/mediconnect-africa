package com.mediconnect.notificationservice.kafka.event;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientCreatedEvent {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
}