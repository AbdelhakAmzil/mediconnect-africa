package com.mediconnect.consultationservice.controller;

import com.mediconnect.consultationservice.dto.*;
import com.mediconnect.consultationservice.service.ConsultationService;
import lombok.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/consultations")
@RequiredArgsConstructor
public class ConsultationController {

    private final ConsultationService consultationService;

    @PostMapping
    public ResponseEntity<ConsultationResponse> create(
            @RequestBody ConsultationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(consultationService.createConsultation(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConsultationResponse> getById(@PathVariable String id) {
        return ResponseEntity.ok(consultationService.getById(id));
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<ConsultationResponse>> getByPatient(
            @PathVariable Long patientId) {
        return ResponseEntity.ok(consultationService.getByPatient(patientId));
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<ConsultationResponse>> getByDoctor(
            @PathVariable Long doctorId) {
        return ResponseEntity.ok(consultationService.getByDoctor(doctorId));
    }

    @PutMapping("/{id}/start")
    public ResponseEntity<ConsultationResponse> start(@PathVariable String id) {
        return ResponseEntity.ok(consultationService.startConsultation(id));
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<ConsultationResponse> complete(
            @PathVariable String id,
            @RequestBody CompleteRequest request) {
        return ResponseEntity.ok(consultationService.completeConsultation(
                id, request.getDiagnosis(),
                request.getPrescription(), request.getNotes()));
    }
}