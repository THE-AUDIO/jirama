package com.phenix.jirama.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data // Génère les getters, setters, toString, equals et hashCode
@NoArgsConstructor // Génère un constructeur sans arguments
@AllArgsConstructor // Génère un constructeur avec tous les arguments
public class  PaymentToMvolaModel{
    private String amount; // Utilisation de String pour correspondre au JSON, mais BigDecimal serait mieux pour l'argent
    private String currency;
    private String descriptionText;
    private String requestingOrganisationTransactionReference;

    // Utilisation de Instant pour une gestion robuste des dates/heures au format ISO 8601 avec fuseau horaire
    // Jackson gère automatiquement la conversion de "2025-07-04T22:50:00.000Z" en Instant
    private String requestDate;

    private String originalTransactionReference;
    private List<KeyValue> debitParty; // Liste d'objets KeyValue
    private List<KeyValue> creditParty; // Liste d'objets KeyValue
    private List<KeyValue> metadata; // Liste d'objets KeyValue
}

