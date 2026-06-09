package com.mediconnect.iotservice.kafka;

import com.mediconnect.iotservice.dto.IoTAlert;
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
public class IoTProducer {

    private final KafkaTemplate<String, IoTAlert> kafkaTemplate;

    public void sendAlert(IoTAlert alert) {
        log.info("📤 Publishing iot-alert for patient: {}", alert.getPatientId());
        Message<IoTAlert> message = MessageBuilder
                .withPayload(alert)
                .setHeader(KafkaHeaders.TOPIC, "iot-alert")
                .build();
        kafkaTemplate.send(message);
    }
}