package ar.edu.itba.paw.models;

public class FrameworkVote {
    private long id;
    private long frameworkId;
    private long userId;
    private int stars;

    public void setStars(int stars) {
        this.stars = stars;
    }

    @Override
    public String toString() {
        return "Vote{" +
                "voteId=" + id +
                ", frameworkId=" + frameworkId +
                ", userId=" + userId +
                ", stars=" + stars +
                '}';
    }

    public FrameworkVote(long voteId, long frameworkId, long userId, int stars) {
        this.id = voteId;
        this.frameworkId = frameworkId;
        this.userId = userId;
        this.stars = stars;
    }

    public long getVoteId() {
        return id;
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
