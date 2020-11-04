package ar.edu.itba.paw.webapp.form.posts;

import javax.validation.constraints.NotNull;

public class UpVoteForm {
    @NotNull
    private long upVotePostId;

    public long getUpVotePostId() {
        return upVotePostId;
    }

    public void setUpVotePostId(long upVotePostId) {
        this.upVotePostId = upVotePostId;
    }
}
