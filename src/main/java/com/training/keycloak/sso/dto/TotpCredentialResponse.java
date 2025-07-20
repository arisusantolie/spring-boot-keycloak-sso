package com.training.keycloak.sso.dto;

import java.util.List;

public class TotpCredentialResponse {
    private List<String> deviceName ;

    public List<String> getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(List<String> deviceName) {
        this.deviceName = deviceName;
    }

    public TotpCredentialResponse() {
    }

    public TotpCredentialResponse(List<String> deviceName) {
        this.deviceName = deviceName;
    }
}
