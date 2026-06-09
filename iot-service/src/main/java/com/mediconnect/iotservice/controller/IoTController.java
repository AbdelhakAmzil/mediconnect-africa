package com.mediconnect.iotservice.controller;

import com.mediconnect.iotservice.dto.IoTData;
import com.mediconnect.iotservice.service.IoTDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/iot")
@RequiredArgsConstructor
public class IoTController {

    private final IoTDataService iotDataService;

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