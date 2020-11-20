package ar.edu.itba.paw.webapp.form.mod_page;

import javax.validation.constraints.NotNull;

public class RevokePromotionForm {
    @NotNull
    private long revokePromotionVerificationId;

    public long getRevokePromotionVerificationId() {
        return revokePromotionVerificationId;
    }

    public void setRevokePromotionVerificationId(long revokePromotionVerificationId) {
        this.revokePromotionVerificationId = revokePromotionVerificationId;
    }
}
