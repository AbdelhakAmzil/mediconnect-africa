package com.mediconnect.consultationservice.repository;

import com.mediconnect.consultationservice.entity.Consultation;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface ConsultationRepository extends MongoRepository<Consultation, String> {
    List<Consultation> findByPatientId(Long patientId);
    List<Consultation> findByDoctorId(Long doctorId);
    List<Consultation> findByAppointmentId(Long appointmentId);
}