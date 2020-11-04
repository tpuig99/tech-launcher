package ar.edu.itba.paw.webapp.form.frameworks;

import javax.validation.constraints.NotNull;

public class ReplyForm {
    @NotNull
    private long replyFrameworkId;
    @NotNull
    private String replyComment;
    @NotNull
    private Long replyCommentId;

    public long getReplyFrameworkId() {
        return replyFrameworkId;
    }

    public void setReplyFrameworkId(long replyFrameworkId) {
        this.replyFrameworkId = replyFrameworkId;
    }

    public String getReplyComment() {
        return replyComment;
    }

    public void setReplyComment(String replyComment) {
        this.replyComment = replyComment;
    }

    public Long getReplyCommentId() {
        return replyCommentId;
    }

    public void setReplyCommentId(Long replyCommentId) {
        this.replyCommentId = replyCommentId;
    }
}
