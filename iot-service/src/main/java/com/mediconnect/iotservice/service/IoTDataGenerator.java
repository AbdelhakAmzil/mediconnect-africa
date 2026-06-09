package com.mediconnect.iotservice.service;

import com.mediconnect.iotservice.dto.IoTData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class IoTDataGenerator {

    private final IoTDataService iotDataService;
    private final Random random = new Random();

    public int generateTestData(int count) {
        String[] deviceTypes = {"HEART_RATE", "BLOOD_PRESSURE", "TEMPERATURE", "SPO2"};
        String[] units = {"bpm", "mmHg", "°C", "%"};
        Long[] patientIds = {1L, 2L, 3L, 4L, 5L};

        for (int i = 0; i < count; i++) {
            int typeIndex = random.nextInt(4);
            String deviceType = deviceTypes[typeIndex];
            double value = generateValue(deviceType);

            IoTData data = IoTData.builder()
                    .patientId(patientIds[random.nextInt(5)])
                    .deviceId("DEVICE-" + String.format("%03d", random.nextInt(10) + 1))
                    .deviceType(deviceType)
                    .value(value)
                    .unit(units[typeIndex])
                    .alert(false)
                    .build();

            iotDataService.processData(data);

            if (i % 100 == 0) {
                log.info("📊 Generated {}/{} records", i, count);
            }
        }

        log.info("✅ Generated {} IoT records", count);
        return count;
    }

    private double generateValue(String deviceType) {
        return switch (deviceType) {
            case "HEART_RATE" -> 50 + random.nextDouble() * 100; // 50-150 bpm
            case "BLOOD_PRESSURE" -> 60 + random.nextDouble() * 120; // 60-180 mmHg
            case "TEMPERATURE" -> 35.0 + random.nextDouble() * 6; // 35-41°C
            case "SPO2" -> 85 + random.nextDouble() * 15; // 85-100%
            default -> random.nextDouble() * 100;
        };
    }
}