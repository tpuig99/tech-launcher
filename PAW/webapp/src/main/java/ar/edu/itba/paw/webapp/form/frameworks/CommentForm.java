package ar.edu.itba.paw.webapp.form.frameworks;

public class CommentForm {

    private long commentFrameworkId;

    private String content;

    private Long commentId;

    public long getCommentFrameworkId() {
        return commentFrameworkId;
    }

    public void setCommentFrameworkId(long commentFrameworkId) {
        this.commentFrameworkId = commentFrameworkId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }
}
