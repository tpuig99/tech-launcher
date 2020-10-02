package ar.edu.itba.paw.webapp.form.register;

import com.sun.org.apache.xpath.internal.operations.Bool;

public class LoginForm {
    private String username;
    private String password;
    private Boolean rememberme = false;
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getRememberme() {
        return rememberme;
    }

    public void setRememberme(Boolean rememberme) {
        this.rememberme = rememberme;
    }
}
