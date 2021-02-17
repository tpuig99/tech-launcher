package ar.edu.itba.paw.webapp.form.mod_page;

import javax.validation.constraints.NotNull;

public class DeleteCommentForm {

    private long deleteCommentId;

    public long getDeleteCommentId() {
        return deleteCommentId;
    }

    public void setDeleteCommentId(long deleteCommentId) {
        this.deleteCommentId = deleteCommentId;
    }
}
