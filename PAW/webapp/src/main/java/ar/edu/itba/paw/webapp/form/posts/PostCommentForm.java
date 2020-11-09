package ar.edu.itba.paw.webapp.form.posts;

import javax.validation.constraints.NotNull;

public class PostCommentForm {
    @NotNull
    private long commentPostId;
    @NotNull
    private String comment;
    private Long commentId;

    public long getCommentPostId() {
        return commentPostId;
    }

    public void setCommentPostId(long commentPostId) {
        this.commentPostId = commentPostId;
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
