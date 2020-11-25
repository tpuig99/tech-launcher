package ar.edu.itba.paw.webapp.form.posts;

import javax.validation.constraints.NotNull;

public class DeletePostCommentForm {
    @NotNull
    private long commentDeletePostId;
    @NotNull
    private Long commentDeleteId;

    public long getCommentDeletePostId() {
        return commentDeletePostId;
    }

    public void setCommentDeletePostId(long commentDeletePostId) {
        this.commentDeletePostId = commentDeletePostId;
    }

    public Long getCommentDeleteId() {
        return commentDeleteId;
    }

    public void setCommentDeleteId(Long commentDeleteId) {
        this.commentDeleteId = commentDeleteId;
    }
}
