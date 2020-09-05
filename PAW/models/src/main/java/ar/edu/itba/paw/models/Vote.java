package ar.edu.itba.paw.models;

public class Vote {
    private long voteId;
    private long frameworkId;
    private long userId;
    private int stars;

    public Vote(long voteId, long frameworkId, long userId, int stars) {
        this.voteId = voteId;
        this.frameworkId = frameworkId;
        this.userId = userId;
        this.stars = stars;
    }

    public long getVoteId() {
        return voteId;
    }

    public long getFrameworkId() {
        return frameworkId;
    }

    public long getUserId() {
        return userId;
    }

    public int getStars() {
        return stars;
    }
}
