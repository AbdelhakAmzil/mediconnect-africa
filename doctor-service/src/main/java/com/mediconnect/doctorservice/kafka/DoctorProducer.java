package com.mediconnect.doctorservice.kafka;

import com.mediconnect.doctorservice.entity.Doctor;
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
public class DoctorProducer {

    private final KafkaTemplate<String, Doctor> kafkaTemplate;

    public void sendDoctorCreatedEvent(Doctor doctor) {
        log.info("📤 Publishing doctor-created event for doctor id: {}", doctor.getId());
        Message<Doctor> message = MessageBuilder
                .withPayload(doctor)
                .setHeader(KafkaHeaders.TOPIC, "doctor-created")
                .build();
        kafkaTemplate.send(message);
    }
}