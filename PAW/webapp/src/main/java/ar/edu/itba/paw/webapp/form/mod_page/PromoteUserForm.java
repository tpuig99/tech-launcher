package ar.edu.itba.paw.webapp.form.mod_page;

import javax.validation.constraints.NotNull;

public class PromoteUserForm {
    @NotNull
    private long promoteUserVerificationId;

    public long getPromoteUserVerificationId() {
        return promoteUserVerificationId;
    }

    public void setPromoteUserVerificationId(long promoteUserVerificationId) {
        this.promoteUserVerificationId = promoteUserVerificationId;
    }
}
