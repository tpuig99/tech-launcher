package ar.edu.itba.paw.webapp.form.frameworks;

public class DownVoteForm {
    private long downVoteFrameworkId;

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
