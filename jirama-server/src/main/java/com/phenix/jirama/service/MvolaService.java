package com.phenix.jirama.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.phenix.jirama.models.KeyValue;
import com.phenix.jirama.models.Payement;
import com.phenix.jirama.models.PaymentToMvolaModel;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.*;

@Service
public class MvolaService {

    public Map<String, String> authMvola() {

        // === Configuration ===
        String apiUrl = "https://devapi.mvola.mg/token";
        String consumerKey = "s7vYu7WDfFLiIyuA96BXN1j4Vmwa";
        String consumerSecret = "GfEZ4kjebbGzWjjnAKsWaE7jVf4a";

        // === Headers ===
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(consumerKey, consumerSecret);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setCacheControl(CacheControl.noCache());

        // === Body (x-www-form-urlencoded format) ===
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "client_credentials");
        body.add("scope", "EXT_INT_MVOLA_SCOPE");

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);

        // === Envoi de la requête ===
        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.POST,
                    entity,
                    Map.class
            );
            Map<String,String> data = new HashMap<>();
            //assert response.getBody() != null;
            String token = (String) response.getBody().get("access_token");
            String tokenType = (String) response.getBody().get("token_type");
            String scope = (String) response.getBody().get("scope");
            String expires_in = response.getBody().get("expires_in").toString();
            data.put("token",token);
            data.put("token_type",tokenType);
            data.put("scope",scope);
            data.put("expires_in",expires_in);
            return data;

        } catch (Exception e) {
            System.err.println("Erreur AuthMvola: " + e.getMessage());

            // Retourne une Map vide ou une Map avec un message d’erreur
            Map<String, String> error = new HashMap<>();
            error.put("error", "Échec de l'authentification Mvola");
            error.put("details", e.getMessage());
            return error;
        }
    }
    public  Map<String,String> InitiateTransaction( @RequestBody  Payement body, @RequestHeader Map<String, String> head){
        HttpHeaders headers = new HttpHeaders();
        String apiUrl = "https://devapi.mvola.mg/mvola/mm/transactions/type/merchantpay/1.0.0/";
        // ATTENTION: Le token doit être géré de manière sécurisée (pas en dur dans le code)
        // et rafraîchi si nécessaire.
        String token = "Bearer "+ head.get("mvolatoken");
        headers.set("Accept","*/*");
        headers.set("Accept-Encoding","gzip, deflate, br");
        headers.set("Connecion","keep-alive");
        headers.set("X-CorrelationID", "4d1dbf0a-46dc-4a84-86f6-9811f18b9c70"); // Utilisez un ID unique pour chaque transaction
        headers.set("UserAccountIdentifier", "msisdn;"+ body.getRecipient());
        headers.set("amount", "5000");
        headers.set("curency","Ar");
        headers.set("Authorization", token); // Pas besoin de .toString() sur une String
        headers.set("version", "1.0");
        headers.set("UserLanguage", "mg");
        headers.set("Content-Type", "application/json"); // Déjà défini par setContentType

        // ATTENTION: Vérifiez la documentation Mvola pour le format exact de cet en-tête.
        // "msisdn 0342645077" semble incorrect, souvent c'est juste le numéro ou "msisdn:numéro"


        // ATTENTION: partnerName est déjà dans les metadata, est-il nécessaire en en-tête aussi?
        // Vérifiez la doc Mvola pour éviter la redondance ou les conflits.
        headers.set("partnerName", "FPhenix");
        headers.set("Cache-Control", "no-cache");

        // Construction du corps de la requête Mvola
        PaymentToMvolaModel payment = new PaymentToMvolaModel();
        payment.setAmount(body.getAmount());
        payment.setCurrency(body.getCurrency());
        payment.setDescriptionText(body.getDescriptionText());
        payment.setRequestingOrganisationTransactionReference(body.getRequestingOrganisationTransactionReference());
        payment.setRequestDate("2025-07-04T22:50:00.000Z");
        payment.setOriginalTransactionReference(body.getOriginalTransactionReference());

        // Sender et Recipient
        KeyValue debitParty = new KeyValue("msisdn", body.getSender());
        KeyValue creditParty = new KeyValue("msisdn", body.getRecipient());

        payment.setDebitParty(Collections.singletonList(debitParty));
        payment.setCreditParty(Collections.singletonList(creditParty));

        // Metadata
        KeyValue metadataAmountFc = new KeyValue("amountFc", "1");
        KeyValue metadataPartnerName = new KeyValue("partnerName", "FPhenix");
        KeyValue metadataFc = new KeyValue("fc", "USD");
        KeyValue metadataCallbackUrl = new KeyValue("callbackUrl", "https://tonserveur.com/notification");

        payment.setMetadata(Arrays.asList(
                metadataPartnerName,
                metadataFc,
                metadataAmountFc,
                metadataCallbackUrl
        ));

        System.out.println("Objet PaymentToMvolaModel avant envoi : " + payment);

        // --- Conversion de l'objet PaymentToMvolaModel en JSON String pour vérification/logging ---
        // Cette ObjectMapper est pour la démonstration/logging UNIQUEMENT.
        // Le RestTemplate injecté utilise déjà son propre ObjectMapper configuré.
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule()); // S'assurer que ce mapper gère Instant
        try {
            String jsonOutput = mapper.writeValueAsString(payment);
            System.out.println("\nObjet PaymentToMvolaModel converti en JSON pour vérification : ");
            System.out.println(jsonOutput);
        } catch (Exception jsonEx) {
            System.err.println("Erreur lors de la conversion de l'objet en JSON pour logging : " + jsonEx.getMessage());
            jsonEx.printStackTrace();
        }

        HttpEntity<PaymentToMvolaModel> entity = new HttpEntity<>(payment, headers);
        RestTemplate restTemplate = new RestTemplate();


        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            Map<String, String> data = new HashMap<>();
            data.put("statusCode", response.getStatusCode().toString());
            data.put("body", response.getBody()); // Le corps est déjà une String
            System.out.println("Réponse Mvola (Statut) : " + response.getStatusCode());
            System.out.println("Réponse Mvola (Corps) : " + response.getBody());
            return data;

        } catch (HttpClientErrorException e) {
            System.err.println("Erreur client lors de l'appel Mvola (HTTP " + e.getStatusCode() + ") : " + e.getResponseBodyAsString());
            // Retourne les détails de l'erreur HTTP client
            Map<String, String> error = new HashMap<>();
            error.put("error", "Échec de l'appel API Mvola");
            error.put("statusCode", e.getStatusCode().toString());
            error.put("details", e.getResponseBodyAsString()); // Corps de la réponse d'erreur de Mvola
            error.put("requestBodySent", payment.toString()); // Utile pour déboguer ce qui a été envoyé
            return error;
        } catch (Exception e) {
            System.err.println("Erreur inattendue lors de l'appel Mvola : " + e.getMessage());
            e.printStackTrace();
            // Retourne une Map avec un message d’erreur générique
            Map<String, String> error = new HashMap<>();
            error.put("error", "Erreur interne lors du traitement de la transaction");
            error.put("details", e.getMessage());
            return error;
        }
    }
}
