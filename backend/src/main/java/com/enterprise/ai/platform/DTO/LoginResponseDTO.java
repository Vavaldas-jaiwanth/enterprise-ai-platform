package com.enterprise.ai.platform.DTO;

public class LoginResponseDTO {
    
    private String authToken;
    private String refreshToken;
    private long expiresIn;
    private boolean mustChangePassword;
    private AuthenticatedUserDTO user;

    public LoginResponseDTO() {
    }

    public LoginResponseDTO(String authToken, String refreshToken, long expiresIn, boolean mustChangePassword, AuthenticatedUserDTO user) {
        this.authToken = authToken;
        this.refreshToken = refreshToken;
        this.expiresIn = expiresIn;
        this.mustChangePassword = mustChangePassword;
        this.user = user;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public boolean isMustChangePassword() {
        return mustChangePassword;
    }

    public void setMustChangePassword(boolean mustChangePassword) {
        this.mustChangePassword = mustChangePassword;
    }

    public AuthenticatedUserDTO getUser() {
        return user;
    }

    public void setUser(AuthenticatedUserDTO user) {
        this.user = user;
    }
}
