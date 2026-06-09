package com.mediconnect.appointmentservice.kafka;

import com.mediconnect.appointmentservice.entity.Appointment;
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
public class AppointmentProducer {

    private final KafkaTemplate<String, Appointment> kafkaTemplate;

    public void sendAppointmentBookedEvent(Appointment appointment) {
        log.info("📤 Publishing appointment-booked event for appointment id: {}", appointment.getId());
        Message<Appointment> message = MessageBuilder
                .withPayload(appointment)
                .setHeader(KafkaHeaders.TOPIC, "appointment-booked")
                .build();
        kafkaTemplate.send(message);
    }
}