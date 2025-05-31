package demo.DTO;

import jakarta.validation.constraints.NotBlank;

public class TokenDto {
    @NotBlank
    private String accessToken;
    @NotBlank
    private String refreshToken;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
