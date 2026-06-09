package com.mediconnect.consultationservice.service;

import com.mediconnect.consultationservice.dto.*;
import com.mediconnect.consultationservice.entity.Consultation;
import com.mediconnect.consultationservice.kafka.ConsultationProducer;
import com.mediconnect.consultationservice.repository.ConsultationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConsultationService {

    private final ConsultationRepository consultationRepository;
    private final ConsultationProducer consultationProducer;

    public ConsultationResponse createConsultation(ConsultationRequest request) {
        Consultation consultation = Consultation.builder()
                .appointmentId(request.getAppointmentId())
                .patientId(request.getPatientId())
                .doctorId(request.getDoctorId())
                .roomToken(UUID.randomUUID().toString())
                .status(Consultation.ConsultationStatus.SCHEDULED)
                .createdAt(LocalDateTime.now())
                .build();

        Consultation saved = consultationRepository.save(consultation);
        log.info("✅ Consultation created with id: {}", saved.getId());
        return toResponse(saved);
    }

    public ConsultationResponse startConsultation(String id) {
        Consultation consultation = consultationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Consultation not found: " + id));
        consultation.setStatus(Consultation.ConsultationStatus.ACTIVE);
        consultation.setStartTime(LocalDateTime.now());
        log.info("🎥 Consultation {} started", id);
        return toResponse(consultationRepository.save(consultation));
    }

    public ConsultationResponse completeConsultation(String id, String diagnosis,
                                                     String prescription, String notes) {
        Consultation consultation = consultationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Consultation not found: " + id));
        consultation.setStatus(Consultation.ConsultationStatus.COMPLETED);
        consultation.setEndTime(LocalDateTime.now());
        consultation.setDiagnosis(diagnosis);
        consultation.setPrescription(prescription);
        consultation.setNotes(notes);

        Consultation saved = consultationRepository.save(consultation);
        log.info("🏁 Consultation {} completed", id);

        try {
            consultationProducer.sendConsultationDoneEvent(saved);
        } catch (Exception e) {
            log.error("❌ Kafka event failed: {}", e.getMessage());
        }

        return toResponse(saved);
    }

    public ConsultationResponse getById(String id) {
        return consultationRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new RuntimeException("Consultation not found: " + id));
    }

    public List<ConsultationResponse> getByPatient(Long patientId) {
        return consultationRepository.findByPatientId(patientId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<ConsultationResponse> getByDoctor(Long doctorId) {
        return consultationRepository.findByDoctorId(doctorId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    private ConsultationResponse toResponse(Consultation c) {
        return ConsultationResponse.builder()
                .id(c.getId())
                .appointmentId(c.getAppointmentId())
                .patientId(c.getPatientId())
                .doctorId(c.getDoctorId())
                .roomToken(c.getRoomToken())
                .status(c.getStatus())
                .diagnosis(c.getDiagnosis())
                .prescription(c.getPrescription())
                .notes(c.getNotes())
                .startTime(c.getStartTime())
                .endTime(c.getEndTime())
                .createdAt(c.getCreatedAt())
                .build();
    }
}