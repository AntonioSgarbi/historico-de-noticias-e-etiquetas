package tech.antoniosgarbi.desafioapi.dto;

public class RefreshDTO {
    private String refreshToken;

    public RefreshDTO() { }

    public RefreshDTO(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
