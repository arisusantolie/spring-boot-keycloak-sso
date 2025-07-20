package com.training.keycloak.sso.dto;

public class TokenResponse {
    private String access_token;
    private String token_type;
    private int expires_in;
    private String refresh_token;
    private String scope;

    // Getters and setters
    public String getAccessToken() { return access_token; }
    public void setAccess_token(String access_token) { this.access_token = access_token; }
    public String getTokenType() { return token_type; }
    public void setToken_type(String token_type) { this.token_type = token_type; }
    public int getExpiresIn() { return expires_in; }
    public void setExpires_in(int expires_in) { this.expires_in = expires_in; }
    public String getRefreshToken() { return refresh_token; }
    public void setRefresh_token(String refresh_token) { this.refresh_token = refresh_token; }
    public String getScope() { return scope; }
    public void setScope(String scope) { this.scope = scope; }
}
