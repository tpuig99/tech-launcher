package ar.edu.itba.paw.webapp.dto;

public class JwtResponseDTO {

    private String token;

    public JwtResponseDTO() {

    }

    public JwtResponseDTO(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}