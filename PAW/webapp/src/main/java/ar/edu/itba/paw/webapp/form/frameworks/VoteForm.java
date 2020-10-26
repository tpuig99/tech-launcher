package ar.edu.itba.paw.webapp.form.frameworks;

public class VoteForm {
    private long frameworkId;
    private int commentId;
    private int vote;

    public long getFrameworkId() {
        return frameworkId;
    }

    public void setFrameworkId(long frameworkId) {
        this.frameworkId = frameworkId;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public int getVote() {
        return vote;
    }

    public void setVote(int vote) {
        this.vote = vote;
    }
}
