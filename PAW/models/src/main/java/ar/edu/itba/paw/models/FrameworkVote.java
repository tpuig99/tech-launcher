package ar.edu.itba.paw.models;

public class FrameworkVote {
    private long id;
    private long frameworkId;
    private long userId;
    private int stars;
    private String frameworkName;
    private FrameworkCategories frameworkCategory;


    public String getFrameworkName() {
        return frameworkName;
    }

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

    public FrameworkVote(long id, long frameworkId, long userId, int stars, String frameworkName, FrameworkCategories frameworkCategory) {
        this.id = id;
        this.frameworkId = frameworkId;
        this.userId = userId;
        this.stars = stars;
        this.frameworkName = frameworkName;
        this.frameworkCategory = frameworkCategory;
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

    public FrameworkCategories getFrameworkCategory() {
        return frameworkCategory;
    }
    public String getCategory(){
        return frameworkCategory.getNameCat();
    }
}
