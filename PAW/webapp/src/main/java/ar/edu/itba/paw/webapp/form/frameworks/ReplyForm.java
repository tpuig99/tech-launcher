package ar.edu.itba.paw.webapp.form.frameworks;

public class ReplyForm {

    private long replyFrameworkId;

    private String replyContent;

    private Long replyCommentId;

    public long getReplyFrameworkId() {
        return replyFrameworkId;
    }

    public void setReplyFrameworkId(long replyFrameworkId) {
        this.replyFrameworkId = replyFrameworkId;
    }

    public String getReplyContent() {
        return replyContent;
    }

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
    }

    public Long getReplyCommentId() {
        return replyCommentId;
    }

    public void setReplyCommentId(Long replyCommentId) {
        this.replyCommentId = replyCommentId;
    }
}
