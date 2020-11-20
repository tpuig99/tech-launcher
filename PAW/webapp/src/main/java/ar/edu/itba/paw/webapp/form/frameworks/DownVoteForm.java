package ar.edu.itba.paw.webapp.form.frameworks;

import javax.validation.constraints.NotNull;

public class DownVoteForm {
    @NotNull
    private long downVoteFrameworkId;
    @NotNull
    private int downVoteCommentId;

    public long getDownVoteFrameworkId() {
        return downVoteFrameworkId;
    }

    public void setDownVoteFrameworkId(long downVoteFrameworkId) {
        this.downVoteFrameworkId = downVoteFrameworkId;
    }

    public int getDownVoteCommentId() {
        return downVoteCommentId;
    }

    public void setDownVoteCommentId(int downVoteCommentId) {
        this.downVoteCommentId = downVoteCommentId;
    }
}
