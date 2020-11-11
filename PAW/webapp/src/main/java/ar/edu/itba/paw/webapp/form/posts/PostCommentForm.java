package ar.edu.itba.paw.webapp.form.posts;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class PostCommentForm {

    @NotNull
    private long commentPostId;

    @NotNull
    @Size(min=1, max=500)
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
