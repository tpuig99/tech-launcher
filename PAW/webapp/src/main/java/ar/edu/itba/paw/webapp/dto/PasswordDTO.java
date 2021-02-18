package ar.edu.itba.paw.webapp.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class PasswordDTO {
    private String token;
    @NotNull
    @Size(min = 6, max = 100)
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
