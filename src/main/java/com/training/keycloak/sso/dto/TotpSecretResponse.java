package com.training.keycloak.sso.dto;

public class TotpSecretResponse {
    private String encodedSecret;
    private String qrCode;

    // Getters and setters
    public String getEncodedSecret() { return encodedSecret; }
    public void setEncodedSecret(String encodedSecret) { this.encodedSecret = encodedSecret; }
    public String getQrCode() { return qrCode; }
    public void setQrCode(String qrCode) { this.qrCode = qrCode; }
}
