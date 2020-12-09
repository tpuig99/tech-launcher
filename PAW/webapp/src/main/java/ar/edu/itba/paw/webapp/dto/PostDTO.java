package ar.edu.itba.paw.webapp.dto;


import ar.edu.itba.paw.models.Post;
import ar.edu.itba.paw.models.PostTag;
import java.util.Date;
import java.util.List;

public class PostDTO {
    private String postTitle;
    private String postDescription;
    private SimpleUserDTO postOwner;
    private Date timestamp;
    private List<PostTag> postTags;
    private List<PostCommentDTO> postComments;
    private long answersAmount, votesUp, votesDown;

    public static PostDTO fromPost(Post post) {
        final PostDTO dto = new PostDTO();
        dto.postTitle = post.getTitle();
        dto.postDescription = post.getDescription();
        dto.postOwner = SimpleUserDTO.fromUser(post.getUser());
        dto.timestamp = post.getTimestamp();
        dto.postTags = post.getPostTags();
        dto.answersAmount = post.getAnswersAmount();
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

    public List<PostTag> getPostTags() {
        return postTags;
    }

    public void setPostTags(List<PostTag> postTags) {
        this.postTags = postTags;
    }

    public long getAnswersAmount() {
        return answersAmount;
    }

    public void setAnswersAmount(long answersAmount) {
        this.answersAmount = answersAmount;
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
}
