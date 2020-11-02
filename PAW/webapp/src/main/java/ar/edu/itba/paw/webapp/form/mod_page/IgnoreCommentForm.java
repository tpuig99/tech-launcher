package ar.edu.itba.paw.webapp.form.mod_page;

import javax.validation.constraints.NotNull;

public class IgnoreCommentForm {
    @NotNull
    private long ignoreCommentId;

    public long getIgnoreCommentId() {
        return ignoreCommentId;
    }

    public void setIgnoreCommentId(long ignoreCommentId) {
        this.ignoreCommentId = ignoreCommentId;
    }
}
