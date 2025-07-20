package com.training.keycloak.sso.dto;

public class TotpDeleteRequest {
    private String deviceName;

    public TotpDeleteRequest() {
    }

    public TotpDeleteRequest(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }
}
