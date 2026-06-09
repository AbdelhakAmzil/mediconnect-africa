package com.mediconnect.notificationservice.kafka;

import com.mediconnect.notificationservice.entity.Notification;
import com.mediconnect.notificationservice.entity.NotificationType;
import com.mediconnect.notificationservice.kafka.event.AppointmentBookedEvent;
import com.mediconnect.notificationservice.kafka.event.PatientCreatedEvent;
import com.mediconnect.notificationservice.repository.NotificationRepository;
import com.mediconnect.notificationservice.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationConsumer {

    private final EmailService emailService;
    private final NotificationRepository notificationRepository;

    @KafkaListener(
            topics = "patient-created",
            groupId = "notification-group",
            properties = {"spring.json.value.default.type=com.mediconnect.notificationservice.kafka.event.PatientCreatedEvent"}
    )
    public void consumePatientCreated(PatientCreatedEvent event) {
        log.info("📨 Received patient-created event for: {}", event.getEmail());

        String subject = "Bienvenue sur MediConnect Africa !";
        String body = String.format(
                "Bonjour %s %s,\n\n" +
                        "Votre compte MediConnect Africa a été créé avec succès.\n" +
                        "Vous pouvez maintenant prendre des rendez-vous et consulter vos médecins en ligne.\n\n" +
                        "Bienvenue dans la famille MediConnect ! 🏥\n\n" +
                        "L'équipe MediConnect Africa",
                event.getFirstName(), event.getLastName()
        );

        emailService.sendEmail(event.getEmail(), subject, body);

        notificationRepository.save(Notification.builder()
                .userId(event.getId())
                .email(event.getEmail())
                .title(subject)
                .message(body)
                .type(NotificationType.EMAIL)
                .sent(true)
                .build());

        log.info("✅ Welcome email sent to: {}", event.getEmail());
    }

    @KafkaListener(
            topics = "appointment-booked",
            groupId = "notification-group",
            properties = {"spring.json.value.default.type=com.mediconnect.notificationservice.kafka.event.AppointmentBookedEvent"}
    )
    public void consumeAppointmentBooked(AppointmentBookedEvent event) {
        log.info("📨 Received appointment-booked event for patient: {}", event.getPatientId());

        String subject = "Confirmation de votre rendez-vous";
        String body = String.format(
                "Votre rendez-vous a été confirmé.\n\n" +
                        "📅 Date : %s\n" +
                        "🏥 Motif : %s\n\n" +
                        "Merci de vous présenter 10 minutes avant l'heure.\n\n" +
                        "L'équipe MediConnect Africa",
                event.getAppointmentDate(), event.getReason()
        );

        notificationRepository.save(Notification.builder()
                .userId(event.getPatientId())
                .title(subject)
                .message(body)
                .type(NotificationType.EMAIL)
                .sent(true)
                .build());

        log.info("✅ Appointment notification saved for patient: {}", event.getPatientId());
    }
}