package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.PostComment;

import java.util.Date;

public class PostCommentDTO {
    private long postCommentId;
    private String description;
    private Date timestamp;
    private Long reference;
    private SimpleUserDTO user;
    private Long postId;
    private Long votesUp;
    private Long votesDown;

    public static PostCommentDTO fromComment(PostComment comment){
        final PostCommentDTO postComment = new PostCommentDTO();
        postComment.postCommentId = comment.getPostCommentId();
        postComment.postId = comment.getPost().getPostId();
        postComment.description = comment.getDescription();
        postComment.timestamp = comment.getTimestamp();
        postComment.reference = comment.getReference();
        postComment.user = SimpleUserDTO.fromUser(comment.getUser());
        postComment.votesUp = comment.getVotesUp();
        postComment.votesDown = comment.getVotesDown();
        return postComment;
    }

    public long getPostCommentId() {
        return postCommentId;
    }

    public void setPostCommentId(long postCommentId) {
        this.postCommentId = postCommentId;

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Long getReference() {
        return reference;
    }

    public void setReference(Long reference) {
        this.reference = reference;
    }

    public SimpleUserDTO getUser() {
        return user;
    }

    public void setUser(SimpleUserDTO user) {
        this.user = user;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Long getVotesUp() {
        return votesUp;
    }

    public void setVotesUp(Long votesUp) {
        this.votesUp = votesUp;
    }

    public Long getVotesDown() {
        return votesDown;
    }

    public void setVotesDown(Long votesDown) {
        this.votesDown = votesDown;
    }
}
