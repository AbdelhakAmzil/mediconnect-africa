package com.mediconnect.patientservice.kafka;

import com.mediconnect.patientservice.entity.Patient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PatientProducer {

    private final KafkaTemplate<String, Patient> kafkaTemplate;

    public void sendPatientCreatedEvent(Patient patient) {
        log.info("📤 Publishing patient-created event for patient id: {}", patient.getId());
        Message<Patient> message = MessageBuilder
                .withPayload(patient)
                .setHeader(KafkaHeaders.TOPIC, "patient-created")
                .build();
        kafkaTemplate.send(message);
    }
}