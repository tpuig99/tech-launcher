package ar.edu.itba.paw.webapp.form.mod_page;

import javax.validation.constraints.NotNull;

public class RejectUserForm {
    @NotNull
    private long rejectUserVerificationId;

    public long getRejectUserVerificationId() {
        return rejectUserVerificationId;
    }

    public void setRejectUserVerificationId(long rejectUserVerificationId) {
        this.rejectUserVerificationId = rejectUserVerificationId;
    }
}
