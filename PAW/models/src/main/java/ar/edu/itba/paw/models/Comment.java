package ar.edu.itba.paw.models;

import java.sql.Timestamp;

public class Comment {
    private long commentId;
    private long frameworkId;
    private long userId;
    private String description;
    private long votesUp;
    private long votesDown;
    private Timestamp timestamp;
    private Long reference;
    private String frameworkName;
    private String userName;
    private FrameworkCategories category;
    private boolean isVerify;

    public Comment(long commentId, long frameworkId, long userId, String description, long votesUp, long votesDown, Timestamp timestamp, Long reference, String frameworkName, String userName, FrameworkCategories category, boolean isVerify) {
        this.commentId = commentId;
        this.frameworkId = frameworkId;
        this.userId = userId;
        this.description = description;
        this.votesUp = votesUp;
        this.votesDown = votesDown;
        this.timestamp = timestamp;
        this.reference = reference;
        this.frameworkName = frameworkName;
        this.userName = userName;
        this.category = category;
        this.isVerify = isVerify;
    }

    public long getCommentId() {
        return commentId;
    }

    public long getFrameworkId() {
        return frameworkId;
    }

    public long getUserId() {
        return userId;
    }

    public String getDescription() {
        return description;
    }

    public long getVotesUp() {
        return votesUp;
    }

    public long getVotesDown() {
        return votesDown;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public Long getReference() {
        return reference;
    }

    public String getFrameworkName() {
        return frameworkName;
    }

    public String getUserName() {
        return userName;
    }

    public String getCategory(){
        return category.getNameCat();
    }
    public FrameworkCategories getEnumCategory() {
        return category;
    }

    public boolean isVerify() {
        return isVerify;
    }
}
