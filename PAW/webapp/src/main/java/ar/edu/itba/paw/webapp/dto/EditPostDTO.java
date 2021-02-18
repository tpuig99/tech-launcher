package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Post;
import ar.edu.itba.paw.models.PostTag;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.stream.Collectors;

public class EditPostDTO {

    @NotEmpty
    @Size(min = 3, max = 200)
    private String postTitle;

    @NotEmpty
    @Size(min=1, max = 5000)
    private String postDescription;

    private List<PostTagDTO> postTags;

    private List<PostTagDTO> allTags;

    public static EditPostDTO fromPost(Post post, List<PostTag> tags ) {
        EditPostDTO dto = new EditPostDTO();
        dto.postTitle = post.getTitle();
        dto.postDescription = post.getDescription();
        dto.postTags = post.getPostTags().stream().map(PostTagDTO::fromPostTag).collect(Collectors.toList());
        dto.allTags = tags.stream().map(PostTagDTO::fromPostTag).collect(Collectors.toList());
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

    public List<PostTagDTO> getPostTags() {
        return postTags;
    }

    public void setPostTags(List<PostTagDTO> postTags) {
        this.postTags = postTags;
    }

    public List<PostTagDTO> getAllTags() {
        return allTags;
    }

    public void setAllTags(List<PostTagDTO> allTags) {
        this.allTags = allTags;
    }
}
