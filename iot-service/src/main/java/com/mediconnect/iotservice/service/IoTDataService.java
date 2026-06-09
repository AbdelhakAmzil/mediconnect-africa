package com.mediconnect.iotservice.service;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import com.mediconnect.iotservice.dto.IoTAlert;
import com.mediconnect.iotservice.dto.IoTData;
import com.mediconnect.iotservice.kafka.IoTProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class IoTDataService {

    private final InfluxDBClient influxDBClient;
    private final IoTProducer iotProducer;

    @Value("${influxdb.bucket:iot_data}")
    private String bucket;

    public void processData(IoTData data) {
        data.setTimestamp(System.currentTimeMillis());

        // Sauvegarder dans InfluxDB
        saveToInfluxDB(data);

        // Vérifier les alertes
        checkAlerts(data);

        log.info("✅ IoT data processed - patient: {}, device: {}, value: {} {}",
                data.getPatientId(), data.getDeviceType(),
                data.getValue(), data.getUnit());
    }

    private void saveToInfluxDB(IoTData data) {
        try {
            WriteApiBlocking writeApi = influxDBClient.getWriteApiBlocking();
            Point point = Point
                    .measurement("iot_sensor")
                    .addTag("patientId", String.valueOf(data.getPatientId()))
                    .addTag("deviceId", data.getDeviceId())
                    .addTag("deviceType", data.getDeviceType())
                    .addField("value", data.getValue())
                    .addField("unit", data.getUnit())
                    .addField("alert", data.isAlert())
                    .time(Instant.ofEpochMilli(data.getTimestamp()),
                            WritePrecision.MS);

            writeApi.writePoint(point);
            log.info("💾 Data saved to InfluxDB for patient: {}", data.getPatientId());
        } catch (Exception e) {
            log.error("❌ InfluxDB write error: {}", e.getMessage());
        }
    }

    private void checkAlerts(IoTData data) {
        IoTAlert alert = null;

        switch (data.getDeviceType()) {
            case "HEART_RATE":
                if (data.getValue() > 120 || data.getValue() < 40) {
                    alert = IoTAlert.builder()
                            .patientId(data.getPatientId())
                            .deviceType(data.getDeviceType())
                            .value(data.getValue())
                            .message("⚠️ Fréquence cardiaque anormale: " + data.getValue() + " bpm")
                            .severity(data.getValue() > 150 || data.getValue() < 30
                                    ? "CRITICAL" : "HIGH")
                            .timestamp(data.getTimestamp())
                            .build();
                }
                break;
            case "BLOOD_PRESSURE":
                if (data.getValue() > 180 || data.getValue() < 60) {
                    alert = IoTAlert.builder()
                            .patientId(data.getPatientId())
                            .deviceType(data.getDeviceType())
                            .value(data.getValue())
                            .message("⚠️ Pression artérielle anormale: " + data.getValue() + " mmHg")
                            .severity("HIGH")
                            .timestamp(data.getTimestamp())
                            .build();
                }
                break;
            case "TEMPERATURE":
                if (data.getValue() > 39.5 || data.getValue() < 35.0) {
                    alert = IoTAlert.builder()
                            .patientId(data.getPatientId())
                            .deviceType(data.getDeviceType())
                            .value(data.getValue())
                            .message("⚠️ Température anormale: " + data.getValue() + "°C")
                            .severity(data.getValue() > 41.0 ? "CRITICAL" : "HIGH")
                            .timestamp(data.getTimestamp())
                            .build();
                }
                break;
            case "SPO2":
                if (data.getValue() < 90) {
                    alert = IoTAlert.builder()
                            .patientId(data.getPatientId())
                            .deviceType(data.getDeviceType())
                            .value(data.getValue())
                            .message("⚠️ SpO2 critique: " + data.getValue() + "%")
                            .severity(data.getValue() < 85 ? "CRITICAL" : "HIGH")
                            .timestamp(data.getTimestamp())
                            .build();
                }
                break;
        }

        if (alert != null) {
            data.setAlert(true);
            log.warn("🚨 ALERT - Patient: {}, {}", data.getPatientId(), alert.getMessage());
            try {
                iotProducer.sendAlert(alert);
            } catch (Exception e) {
                log.error("❌ Kafka alert error: {}", e.getMessage());
            }
        }
    }
}