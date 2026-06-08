package com.mediconnect.gatewayservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @GetMapping("/patients")
    public Mono<String> patientsFallback() {
        return Mono.just("Patient service is temporarily unavailable. Please try again later.");
    }

    @GetMapping("/doctors")
    public Mono<String> doctorsFallback() {
        return Mono.just("Doctor service is temporarily unavailable. Please try again later.");
    }

    @GetMapping("/appointments")
    public Mono<String> appointmentsFallback() {
        return Mono.just("Appointment service is temporarily unavailable. Please try again later.");
    }

    @GetMapping("/consultations")
    public Mono<String> consultationsFallback() {
        return Mono.just("Consultation service is temporarily unavailable. Please try again later.");
    }

    @GetMapping("/ai")
    public Mono<String> aiFallback() {
        return Mono.just("AI service is temporarily unavailable. Please try again later.");
    }
}