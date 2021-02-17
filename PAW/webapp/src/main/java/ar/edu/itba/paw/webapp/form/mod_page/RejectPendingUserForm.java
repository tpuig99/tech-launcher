package ar.edu.itba.paw.webapp.form.mod_page;

import javax.validation.constraints.NotNull;

public class RejectPendingUserForm {

    private long rejectPendingUserVerificationId;

    public long getRejectPendingUserVerificationId() {
        return rejectPendingUserVerificationId;
    }

    public void setRejectPendingUserVerificationId(long rejectUserVerificationId) {
        this.rejectPendingUserVerificationId = rejectUserVerificationId;
    }
}
