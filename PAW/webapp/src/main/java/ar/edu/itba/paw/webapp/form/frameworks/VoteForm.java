package ar.edu.itba.paw.webapp.form.frameworks;

import javax.validation.constraints.NotNull;

public class VoteForm {
    @NotNull
    private long frameworkId;
    @NotNull
    private int commentId;
    @NotNull
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
