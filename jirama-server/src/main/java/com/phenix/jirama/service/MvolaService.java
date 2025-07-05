package com.phenix.jirama.service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
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
}
