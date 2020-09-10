package ar.edu.itba.paw.models;

public class CommentVote {
    long commentVoteId;
    long commentId;
    long userId;
    int vote;

    public CommentVote(long commentVoteId, long commentId, long userId, int vote) {
        this.commentVoteId = commentVoteId;
        this.commentId = commentId;
        this.userId = userId;
        this.vote = vote;
    }

    public long getCommentVoteId() {
        return commentVoteId;
    }

    public long getCommentId() {
        return commentId;
    }

    public long getUserId() {
        return userId;
    }

    public int getVote() {
        return vote;
    }

    public boolean isVoteUp() {
        return vote==1;
    }

}
