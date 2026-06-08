package com.mediconnect.patientservice.repository;

import com.mediconnect.patientservice.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByEmail(String email);
    Optional<Patient> findByKeycloakId(String keycloakId);
    boolean existsByEmail(String email);
}