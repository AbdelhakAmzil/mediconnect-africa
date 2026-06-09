package com.mediconnect.consultationservice.controller;

import lombok.Data;

@Data
public class CompleteRequest {
    private String diagnosis;
    private String prescription;
    private String notes;
}