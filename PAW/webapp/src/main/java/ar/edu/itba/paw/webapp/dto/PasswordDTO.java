package ar.edu.itba.paw.webapp.dto;

public class PasswordDTO {
    private String token;
    private String password;

    public PasswordDTO() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
