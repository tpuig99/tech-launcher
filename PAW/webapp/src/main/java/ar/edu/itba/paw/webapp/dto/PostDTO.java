package ar.edu.itba.paw.webapp.dto;


import ar.edu.itba.paw.models.Post;
import ar.edu.itba.paw.models.PostTag;

import javax.ws.rs.core.UriInfo;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class PostDTO {
    private String postTitle;
    private String postDescription;
    private SimpleUserDTO postOwner;
    private Date timestamp;
    private List<PostTagDTO> postTags;
    private List<PostCommentDTO> postComments;
    private long votesUp, votesDown;
    private int loggedVote;

    public static PostDTO fromPost(Post post, UriInfo uriInfo) {
        final PostDTO dto = new PostDTO();
        dto.postTitle = post.getTitle();
        dto.postDescription = post.getDescription();
        dto.postOwner = SimpleUserDTO.fromUser(post.getUser(),uriInfo);
        dto.timestamp = post.getTimestamp();
        dto.postTags = post.getPostTags().stream().map(PostTagDTO::fromPostTag).collect(Collectors.toList());
        dto.votesUp = post.getVotesUp();
        dto.votesDown = post.getVotesDown();
        return dto;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPostDescription() {
        return postDescription;
    }

    public void setPostDescription(String postDescription) {
        this.postDescription = postDescription;
    }

    public SimpleUserDTO getPostOwner() {
        return postOwner;
    }

    public void setPostOwner(SimpleUserDTO postOwner) {
        this.postOwner = postOwner;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public List<PostTagDTO> getPostTags() {
        return postTags;
    }

    public void setPostTags(List<PostTagDTO> postTags) {
        this.postTags = postTags;
    }

    public long getVotesUp() {
        return votesUp;
    }

    public void setVotesUp(long votesUp) {
        this.votesUp = votesUp;
    }

    public long getVotesDown() {
        return votesDown;
    }

    public void setVotesDown(long votesDown) {
        this.votesDown = votesDown;
    }

    public List<PostCommentDTO> getPostComments() {
        return postComments;
    }

    public void setPostComments(List<PostCommentDTO> postComments) {
        this.postComments = postComments;
    }

    public int getLoggedVote() {
        return loggedVote;
    }

    public void setLoggedVote(int loggedVote) {
        this.loggedVote = loggedVote;
    }
}
