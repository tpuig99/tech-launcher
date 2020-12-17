package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.PostComment;

import javax.ws.rs.core.UriInfo;
import java.util.Date;

public class PostCommentDTO {
    private String description;
    private Date timestamp;
    private Long reference;
    private SimpleUserDTO user;
    private String userLocation;
    private Long postId;
    private Long votesUp;
    private Long votesDown;
    private String location;

    public static PostCommentDTO fromComment(PostComment comment, UriInfo uriInfo){
        final PostCommentDTO postComment = new PostCommentDTO();
        postComment.postId = comment.getPost().getPostId();
        postComment.description = comment.getDescription();
        postComment.timestamp = comment.getTimestamp();
        postComment.reference = comment.getReference();
        postComment.user = SimpleUserDTO.fromUser(comment.getUser());
        postComment.votesUp = comment.getVotesUp();
        postComment.votesDown = comment.getVotesDown();
        postComment.userLocation = uriInfo.getBaseUriBuilder().path("users/"+comment.getUser().getId()).build().toString();
        postComment.location = uriInfo.getBaseUriBuilder().path("/posts/" + comment.getPost().getPostId()).build().toString();
        return postComment;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(String userLocation) {
        this.userLocation = userLocation;
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
