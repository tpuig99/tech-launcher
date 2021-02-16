package ar.edu.itba.paw.webapp.form.posts;

import javax.validation.constraints.NotNull;

public class DownVoteForm {

    private long downVotePostId;

    private long postCommentDownVoteId;

    private long downVoteCommentPostId;

    public long getDownVotePostId() {
        return downVotePostId;
    }

    public void setDownVotePostId(long downVotePostId) {
        this.downVotePostId = downVotePostId;
    }

    public long getPostCommentDownVoteId() {
        return postCommentDownVoteId;
    }

    public void setPostCommentDownVoteId(long postCommentDownVoteId) {
        this.postCommentDownVoteId = postCommentDownVoteId;
    }

    public long getDownVoteCommentPostId() {
        return downVoteCommentPostId;
    }

    public void setDownVoteCommentPostId(long downVoteCommentPostId) {
        this.downVoteCommentPostId = downVoteCommentPostId;
    }
}
