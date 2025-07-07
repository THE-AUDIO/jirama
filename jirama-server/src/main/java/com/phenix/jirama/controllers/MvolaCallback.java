package com.phenix.jirama.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class MvolaCallback {
    @PutMapping("/mvola/notification")
    public ResponseEntity<Void> handleMvolaPutCallback(@RequestBody Map<String, Object> mvolaNotification) {
        System.out.println("--- Notification Mvola (PUT) reçue ---");
        System.out.println("Corps de la notification : " + mvolaNotification);

        // Vous pouvez extraire des informations spécifiques comme le statut
        String transactionStatus = (String) mvolaNotification.get("transactionStatus");
        String serverCorrelationId = (String) mvolaNotification.get("serverCorrelationId");
            
        System.out.println("Statut de la transaction : " + transactionStatus);
        System.out.println("Server Correlation ID : " + serverCorrelationId);
        return ResponseEntity.ok().build();
    }
}
