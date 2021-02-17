package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.webapp.dto.custom_constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserAddDTO {

    @NotNull
    @Size(min = 3, max = 100)
    @Pattern(regexp = "[a-zA-Z0-9]+")
    private String username;

    @NotNull
    @NotEmpty
    @Email
    private String mail;

    @NotNull
    @Size(min = 6, max = 100)
    private String password;

    @NotNull
    @NotEmpty
    @Size(max = 200)
    private String description;

    private byte[] picture;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }
}
