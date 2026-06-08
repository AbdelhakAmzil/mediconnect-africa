package com.mediconnect.doctorservice.repository;

import com.mediconnect.doctorservice.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Optional<Doctor> findByEmail(String email);
    List<Doctor> findBySpecialization(String specialization);
    List<Doctor> findByCity(String city);
    List<Doctor> findByAvailableTrue();
    boolean existsByEmail(String email);
}