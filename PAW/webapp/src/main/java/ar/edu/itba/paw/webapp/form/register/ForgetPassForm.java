package ar.edu.itba.paw.webapp.form.register;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;


public class ForgetPassForm {
    @NotNull
    @NotEmpty
    @ValidEmail
    private String email;
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
}