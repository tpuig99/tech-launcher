package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Comment;

import javax.ws.rs.core.UriInfo;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class CommentDTO {
    private String description;
    private Date date;
    private SimpleUserDTO user;
    private Long votesUp;
    private Long votesDown;
    private Long referenceId;
    private List<CommentDTO> replies;
    private Long frameworkId;
    private String location;
    private String techName;

    public static CommentDTO fromComment(Comment comment, UriInfo uriInfo) {
        final CommentDTO dto = new CommentDTO();
        dto.description = comment.getDescription();
        dto.date = comment.getTimestamp();
        dto.referenceId = comment.getReference();
        dto.user = SimpleUserDTO.fromUser(comment.getUser(), comment.getFramework(),uriInfo);
        dto.votesUp = comment.getVotesUp();
        dto.votesDown = comment.getVotesDown();
        if(comment.getReplies() != null)
            dto.replies = comment.getReplies().stream().map((Comment comment1) -> fromComment(comment1,uriInfo)).collect(Collectors.toList());
        dto.location = uriInfo.getBaseUriBuilder().path("/techs/"+comment.getFrameworkId()+"/comment/"+comment.getCommentId()).build().toString();
        return dto;
    }
    public static CommentDTO fromProfile(Comment comment){
        final CommentDTO dto = new CommentDTO();
        dto.description = comment.getDescription();
        dto.date = comment.getTimestamp();
        dto.location = "/techs/"+comment.getFrameworkId();
        dto.techName = comment.getFrameworkName();
        return dto;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public SimpleUserDTO getUser() {
        return user;
    }

    public void setUser(SimpleUserDTO user) {
        this.user = user;
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

    public Long getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(Long referenceId) {
        this.referenceId = referenceId;
    }

    public List<CommentDTO> getReplies() {
        return replies;
    }

    public void setReplies(List<CommentDTO> replies) {
        this.replies = replies;
    }
    public Long getFrameworkId() {
        return frameworkId;
    }

    public void setFrameworkId(Long frameworkId) {
        this.frameworkId = frameworkId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTechName() {
        return techName;
    }

    public void setTechName(String techName) {
        this.techName = techName;
    }
}
