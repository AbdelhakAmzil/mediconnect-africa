package com.mediconnect.consultationservice.kafka;

import com.mediconnect.consultationservice.entity.Consultation;
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
public class ConsultationProducer {

    private final KafkaTemplate<String, Consultation> kafkaTemplate;

    public void sendConsultationDoneEvent(Consultation consultation) {
        log.info("📤 Publishing consultation-done event for: {}", consultation.getId());
        Message<Consultation> message = MessageBuilder
                .withPayload(consultation)
                .setHeader(KafkaHeaders.TOPIC, "consultation-done")
                .build();
        kafkaTemplate.send(message);
    }
}