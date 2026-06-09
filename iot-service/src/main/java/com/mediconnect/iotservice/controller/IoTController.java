package com.mediconnect.iotservice.controller;

import com.mediconnect.iotservice.dto.IoTData;
import com.mediconnect.iotservice.service.IoTDataGenerator;
import com.mediconnect.iotservice.service.IoTDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/iot")
@RequiredArgsConstructor
public class IoTController {

    private final IoTDataService iotDataService;

    @Autowired
    private IoTDataGenerator iotDataGenerator;

    @PostMapping("/generate/{count}")
    public ResponseEntity<String> generateData(@PathVariable int count) {
        int generated = iotDataGenerator.generateTestData(count);
        return ResponseEntity.ok("✅ Generated " + generated + " IoT records");
    }

    @PostMapping("/data")
    public ResponseEntity<String> receiveData(@RequestBody IoTData data) {
        iotDataService.processData(data);
        return ResponseEntity.ok("✅ Data received and processed");
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("IoT Service is running 📡");
    }
}