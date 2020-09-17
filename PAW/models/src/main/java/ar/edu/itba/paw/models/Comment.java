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

    public Comment(long commentId, long frameworkId, long userId, String description, Timestamp timestamp, Long reference) {
        this.commentId = commentId;
        this.frameworkId = frameworkId;
        this.userId = userId;
        this.description = description;
        this.timestamp = timestamp;
        this.reference = reference;
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

    public void setVotesUp(long votesUp) {
        this.votesUp = votesUp;
    }

    public void setVotesDown(long votesDown) {
        this.votesDown = votesDown;
    }
}
