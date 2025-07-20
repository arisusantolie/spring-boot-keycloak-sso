package com.training.keycloak.sso.services;

import com.training.keycloak.sso.dto.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class Keycloak2faTotp {

    @Value("${kc.base.url}")
    private String kcBaseUrl;

    @Value("${kc.realm}")
    private String kcRealm;

    @Value("${kc.svc.client.id}")
    private String kcSvcClientId;

    @Value("${kc.svc.client.secret}")
    private String kcClientSecret;

    private final RestTemplate restTemplate;

    public Keycloak2faTotp(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // Token caching
    private String accessToken;
    private LocalDateTime tokenExpiry;

    // Get access token with caching
    private String getAccessToken() {
        // Check if token is still valid (with 30 second buffer)
        if (accessToken != null && tokenExpiry != null &&
                LocalDateTime.now().isBefore(tokenExpiry.minus(30, ChronoUnit.SECONDS))) {
            return accessToken;
        }

        // Get new token
        String tokenUrl = String.format("%s/realms/%s/protocol/openid-connect/token", kcBaseUrl, kcRealm);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "client_credentials");
        body.add("client_id", kcSvcClientId);
        body.add("client_secret", kcClientSecret);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<TokenResponse> response = restTemplate.exchange(
                tokenUrl,
                HttpMethod.POST,
                entity,
                TokenResponse.class
        );

        TokenResponse tokenResponse = response.getBody();
        if (tokenResponse != null) {
            this.accessToken = tokenResponse.getAccessToken();
            this.tokenExpiry = LocalDateTime.now().plus(tokenResponse.getExpiresIn(), ChronoUnit.SECONDS);
            return this.accessToken;
        }

        throw new RuntimeException("Failed to obtain access token");
    }

    // Create headers with Authorization
    private HttpHeaders createAuthHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(getAccessToken());
        return headers;
    }

    // 1. Generate TOTP Secret
    public TotpSecretResponse generateTotpSecret(String userId) {
        String url = String.format("%s/realms/%s/totp-api/%s/generate", kcBaseUrl, kcRealm, userId);

        HttpHeaders headers = createAuthHeaders();
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<TotpSecretResponse> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                TotpSecretResponse.class
        );

        return response.getBody();
    }

    // 2. Register TOTP Credential
    public TotpRegisterorDeleteResponse registerTotpCredential(String userId, TotpRegisterRequest request) {
        String url = String.format("%s/realms/%s/totp-api/%s/register", kcBaseUrl, kcRealm, userId);

        HttpHeaders headers = createAuthHeaders();
        HttpEntity<TotpRegisterRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<TotpRegisterorDeleteResponse> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                TotpRegisterorDeleteResponse.class
        );

        return response.getBody();
    }

    // 3. Verify TOTP Code
    public TotpVerifyResponse verifyTotpCode(String userId, TotpVerifyRequest request) {
        String url = String.format("%s/realms/%s/totp-api/%s/verify", kcBaseUrl, kcRealm, userId);

        HttpHeaders headers = createAuthHeaders();
        HttpEntity<TotpVerifyRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<TotpVerifyResponse> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                TotpVerifyResponse.class
        );

        return response.getBody();
    }

    // 4. Delete TOTP Code
    public TotpRegisterorDeleteResponse deleteTotpCredential(String userId, TotpDeleteRequest request) {
        String url = String.format("%s/realms/%s/totp-api/%s/remove-totp", kcBaseUrl, kcRealm, userId);

        HttpHeaders headers = createAuthHeaders();
        HttpEntity<TotpDeleteRequest> entity = new HttpEntity<>(request, headers);

        try{
            ResponseEntity<TotpRegisterorDeleteResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    TotpRegisterorDeleteResponse.class
            );
            System.out.println("Response from deleteTotpCredential: " + response.getBody());
            return response.getBody();

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    // Check if user has TOTP setup (by trying to verify with a dummy code)
    public boolean hasTotpSetup(String userId) {
        String url = String.format("%s/realms/%s/totp-api/%s/get-totp-credentials", kcBaseUrl, kcRealm, userId);

        HttpHeaders headers = createAuthHeaders();
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<TotpCredentialResponse> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                TotpCredentialResponse.class
        );

        if(!response.getBody().getDeviceName().isEmpty()){
            return true;
        }
        return false;
    }


}
