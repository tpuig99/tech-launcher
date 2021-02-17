package ar.edu.itba.paw.webapp.form.frameworks;

import javax.validation.constraints.NotNull;

public class CommentForm {

    private long commentFrameworkId;

    @NotNull
    private String comment;
    private Long commentId;

    public long getCommentFrameworkId() {
        return commentFrameworkId;
    }

    public void setCommentFrameworkId(long commentFrameworkId) {
        this.commentFrameworkId = commentFrameworkId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }
}
