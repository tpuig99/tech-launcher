package ar.edu.itba.paw.webapp.form.mod_page;

import javax.validation.constraints.NotNull;

public class PromotePendingUserForm {

    private long promotePendingUserVerificationId;

    public long getPromotePendingUserVerificationId() {
        return promotePendingUserVerificationId;
    }

    public void setPromotePendingUserVerificationId(long promotePendingUserVerificationId) {
        this.promotePendingUserVerificationId = promotePendingUserVerificationId;
    }
}
