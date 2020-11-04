package ar.edu.itba.paw.webapp.form.posts;

import javax.validation.constraints.NotNull;

public class DownVoteForm {
    @NotNull
    private long downVotePostId;

    public long getDownVotePostId() {
        return downVotePostId;
    }

    public void setDownVotePostId(long downVotePostId) {
        this.downVotePostId = downVotePostId;
    }
}
