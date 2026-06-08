package com.mediconnect.doctorservice.service;

import com.mediconnect.doctorservice.dto.*;
import com.mediconnect.doctorservice.entity.Doctor;
import com.mediconnect.doctorservice.kafka.DoctorProducer;
import com.mediconnect.doctorservice.repository.DoctorRepository;
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
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final DoctorProducer doctorProducer;

    public DoctorResponse createDoctor(DoctorRequest request) {
        if (doctorRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Doctor with email " + request.getEmail() + " already exists");
        }
        Doctor doctor = Doctor.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .specialization(request.getSpecialization())
                .licenseNumber(request.getLicenseNumber())
                .hospital(request.getHospital())
                .city(request.getCity())
                .country(request.getCountry())
                .yearsOfExperience(request.getYearsOfExperience())
                .languages(request.getLanguages())
                .keycloakId(request.getKeycloakId())
                .build();

        Doctor saved = doctorRepository.save(doctor);
        log.info("✅ Doctor created with id: {}", saved.getId());

        try {
            doctorProducer.sendDoctorCreatedEvent(saved);
        } catch (Exception e) {
            log.error("❌ Kafka event failed for doctor {}: {}", saved.getId(), e.getMessage());
        }

        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public DoctorResponse getDoctorById(Long id) {
        return doctorRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<DoctorResponse> getAllDoctors() {
        return doctorRepository.findAll()
                .stream().map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DoctorResponse> getDoctorsBySpecialization(String specialization) {
        return doctorRepository.findBySpecialization(specialization)
                .stream().map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DoctorResponse> getAvailableDoctors() {
        return doctorRepository.findByAvailableTrue()
                .stream().map(this::toResponse)
                .collect(Collectors.toList());
    }

    public DoctorResponse updateDoctor(Long id, DoctorRequest request) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + id));

        doctor.setFirstName(request.getFirstName());
        doctor.setLastName(request.getLastName());
        doctor.setPhone(request.getPhone());
        doctor.setSpecialization(request.getSpecialization());
        doctor.setHospital(request.getHospital());
        doctor.setCity(request.getCity());
        doctor.setCountry(request.getCountry());
        doctor.setYearsOfExperience(request.getYearsOfExperience());
        doctor.setLanguages(request.getLanguages());

        return toResponse(doctorRepository.save(doctor));
    }

    public void deleteDoctor(Long id) {
        doctorRepository.deleteById(id);
        log.info("🗑️ Doctor deleted with id: {}", id);
    }

    private DoctorResponse toResponse(Doctor doctor) {
        return DoctorResponse.builder()
                .id(doctor.getId())
                .firstName(doctor.getFirstName())
                .lastName(doctor.getLastName())
                .email(doctor.getEmail())
                .phone(doctor.getPhone())
                .specialization(doctor.getSpecialization())
                .licenseNumber(doctor.getLicenseNumber())
                .hospital(doctor.getHospital())
                .city(doctor.getCity())
                .country(doctor.getCountry())
                .rating(doctor.getRating())
                .yearsOfExperience(doctor.getYearsOfExperience())
                .available(doctor.isAvailable())
                .languages(doctor.getLanguages())
                .createdAt(doctor.getCreatedAt())
                .build();
    }
}