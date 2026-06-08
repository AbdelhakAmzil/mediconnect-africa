package com.mediconnect.patientservice.service;

import com.mediconnect.patientservice.dto.*;
import com.mediconnect.patientservice.entity.Patient;
import com.mediconnect.patientservice.kafka.PatientProducer;
import com.mediconnect.patientservice.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PatientService {

    private final PatientRepository patientRepository;
    private final PatientProducer patientProducer;

    public PatientResponse createPatient(PatientRequest request) {
        if (patientRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Patient with email " + request.getEmail() + " already exists");
        }
        Patient patient = Patient.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .birthDate(request.getBirthDate())
                .address(request.getAddress())
                .bloodType(request.getBloodType())
                .insuranceId(request.getInsuranceId())
                .allergies(request.getAllergies())
                .keycloakId(request.getKeycloakId())
                .build();

        Patient saved = patientRepository.save(patient);
        log.info("✅ Patient created with id: {}", saved.getId());

        // Publier événement Kafka
        patientProducer.sendPatientCreatedEvent(saved);

        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public PatientResponse getPatientById(Long id) {
        return patientRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<PatientResponse> getAllPatients() {
        return patientRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public PatientResponse updatePatient(Long id, PatientRequest request) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + id));

        patient.setFirstName(request.getFirstName());
        patient.setLastName(request.getLastName());
        patient.setPhone(request.getPhone());
        patient.setBirthDate(request.getBirthDate());
        patient.setAddress(request.getAddress());
        patient.setBloodType(request.getBloodType());
        patient.setInsuranceId(request.getInsuranceId());
        patient.setAllergies(request.getAllergies());

        return toResponse(patientRepository.save(patient));
    }

    public void deletePatient(Long id) {
        patientRepository.deleteById(id);
        log.info("🗑️ Patient deleted with id: {}", id);
    }

    private PatientResponse toResponse(Patient patient) {
        return PatientResponse.builder()
                .id(patient.getId())
                .firstName(patient.getFirstName())
                .lastName(patient.getLastName())
                .email(patient.getEmail())
                .phone(patient.getPhone())
                .birthDate(patient.getBirthDate())
                .address(patient.getAddress())
                .bloodType(patient.getBloodType())
                .insuranceId(patient.getInsuranceId())
                .allergies(patient.getAllergies())
                .createdAt(patient.getCreatedAt())
                .build();
    }
}