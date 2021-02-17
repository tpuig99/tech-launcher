package ar.edu.itba.paw.webapp.form.posts;

import javax.validation.constraints.NotNull;

public class UpVoteForm {

    private long upVotePostId;

    private long postCommentUpVoteId;

    private long upVoteCommentPostId;

    public long getUpVotePostId() {
        return upVotePostId;
    }

    public void setUpVotePostId(long upVotePostId) {
        this.upVotePostId = upVotePostId;
    }

    public long getUpVoteCommentPostId() {
        return upVoteCommentPostId;
    }

    public void setUpVoteCommentPostId(long upVoteCommentPostId) {
        this.upVoteCommentPostId = upVoteCommentPostId;
    }

    public long getPostCommentUpVoteId() {
        return postCommentUpVoteId;
    }

    public void setPostCommentUpVoteId(long postCommentUpVoteId) {
        this.postCommentUpVoteId = postCommentUpVoteId;
    }
}
