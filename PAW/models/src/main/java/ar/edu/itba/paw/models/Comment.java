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
    private long reference;

    public Comment(long commentId, long frameworkId, long userId, String description, long votesUp, long votesDown, Timestamp timestamp, long reference) {
        this.commentId = commentId;
        this.frameworkId = frameworkId;
        this.userId = userId;
        this.description = description;
        this.votesUp = votesUp;
        this.votesDown = votesDown;
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

    public long getReference() {
        return reference;
    }
}
