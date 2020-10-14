package ar.edu.itba.paw.models;

public class ContentVote {
    long contentVoteId;
    long contentId;
    long userId;
    int vote;

    public ContentVote(long contentVoteId, long contentId, long userId, int vote) {
        this.contentVoteId = contentVoteId;
        this.contentId = contentId;
        this.userId = userId;
        this.vote = vote;
    }

    public long getContentVoteId() {
        return contentVoteId;
    }

    public long getContentId() {
        return contentId;
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
