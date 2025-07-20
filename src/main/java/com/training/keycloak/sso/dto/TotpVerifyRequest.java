package com.training.keycloak.sso.dto;

public class TotpVerifyRequest {
    private String deviceName;
    private String code;

    public TotpVerifyRequest() {}

    public TotpVerifyRequest(String deviceName, String code) {
        this.deviceName = deviceName;
        this.code = code;
    }

    // Getters and setters
    public String getDeviceName() { return deviceName; }
    public void setDeviceName(String deviceName) { this.deviceName = deviceName; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
}
