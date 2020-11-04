package ar.edu.itba.paw.webapp.form.frameworks;

import javax.validation.constraints.NotNull;

public class DeleteCommentForm {
    @NotNull
    private long commentDeleteFrameworkId;
    @NotNull
    private Long commentDeleteId;

    public long getCommentDeleteFrameworkId() {
        return commentDeleteFrameworkId;
    }

    public void setCommentDeleteFrameworkId(long commentDeleteFrameworkId) {
        this.commentDeleteFrameworkId = commentDeleteFrameworkId;
    }

    public Long getCommentDeleteId() {
        return commentDeleteId;
    }

    public void setCommentDeleteId(Long commentDeleteId) {
        this.commentDeleteId = commentDeleteId;
    }
}
