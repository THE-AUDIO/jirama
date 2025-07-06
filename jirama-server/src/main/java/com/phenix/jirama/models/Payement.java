package com.phenix.jirama.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "PAYEMENT")
public class Payement {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID id;

    private String amount;
    private String currency;
    private String descriptionText;
    private String requestingOrganisationTransactionReference;
    private ZonedDateTime requestDate;
    private String originalTransactionReference;
    private String sender;
    private String recipient;
}
