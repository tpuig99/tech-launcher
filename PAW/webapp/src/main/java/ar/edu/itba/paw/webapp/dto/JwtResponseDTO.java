package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.User;

public class JwtResponseDTO {

    private String token;
    private String location;

    public JwtResponseDTO() {

    }

    public JwtResponseDTO(String token, User user) {
        this.token = token;
        this.location = "users/"+user.getId();
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}