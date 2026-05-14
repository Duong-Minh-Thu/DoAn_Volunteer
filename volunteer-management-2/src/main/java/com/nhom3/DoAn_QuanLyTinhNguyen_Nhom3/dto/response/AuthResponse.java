package com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.dto.response;

import java.time.LocalDateTime;

public class AuthResponse {
    private String accessToken;
    private String tokenType = "Bearer";
    private String username;
    private String role;

    public AuthResponse() {}

    public String getAccessToken() { return accessToken; }
    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }

    public String getTokenType() { return tokenType; }
    public void setTokenType(String tokenType) { this.tokenType = tokenType; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final AuthResponse r = new AuthResponse();

        public Builder accessToken(String val) { r.accessToken = val; return this; }
        public Builder tokenType(String val) { r.tokenType = val; return this; }
        public Builder username(String val) { r.username = val; return this; }
        public Builder role(String val) { r.role = val; return this; }

        public AuthResponse build() { return r; }
    }
}
