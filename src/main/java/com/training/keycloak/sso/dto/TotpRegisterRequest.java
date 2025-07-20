package com.training.keycloak.sso.dto;

public class TotpRegisterRequest {
    private String deviceName;
    private String encodedSecret;
    private String initialCode;
    private boolean overwrite;

    public TotpRegisterRequest() {}

    public TotpRegisterRequest(String deviceName, String encodedSecret, String initialCode, boolean overwrite) {
        this.deviceName = deviceName;
        this.encodedSecret = encodedSecret;
        this.initialCode = initialCode;
        this.overwrite = overwrite;
    }

    // Getters and setters
    public String getDeviceName() { return deviceName; }
    public void setDeviceName(String deviceName) { this.deviceName = deviceName; }
    public String getEncodedSecret() { return encodedSecret; }
    public void setEncodedSecret(String encodedSecret) { this.encodedSecret = encodedSecret; }
    public String getInitialCode() { return initialCode; }
    public void setInitialCode(String initialCode) { this.initialCode = initialCode; }
    public boolean isOverwrite() { return overwrite; }
    public void setOverwrite(boolean overwrite) { this.overwrite = overwrite; }
}
