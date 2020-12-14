package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Comment;
import ar.edu.itba.paw.models.Framework;
import org.eclipse.persistence.internal.sessions.CommitOrderDependencyNode;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class CommentDTO {
    private String description;
    private Date date;
    private SimpleUserDTO user;
    private Long votes_up;
    private Long votes_down;
    private Long referenceId;
    private List<CommentDTO> replies;
    private Long frameworkId;
    private Long commentId;

    public static CommentDTO fromComment(Comment comment) {
        final CommentDTO dto = new CommentDTO();
        dto.description = comment.getDescription();
        dto.date = comment.getTimestamp();
        dto.referenceId = comment.getReference();
        dto.user = SimpleUserDTO.fromUser(comment.getUser(), comment.getFramework());
        dto.votes_up = comment.getVotesUp();
        dto.votes_down = comment.getVotesDown();
        if(comment.getReplies() != null)
            dto.replies = comment.getReplies().stream().map(CommentDTO::fromComment).collect(Collectors.toList());
        dto.commentId = comment.getCommentId();
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

    public Long getVotes_up() {
        return votes_up;
    }

    public void setVotes_up(Long votes_up) {
        this.votes_up = votes_up;
    }

    public Long getVotes_down() {
        return votes_down;
    }

    public void setVotes_down(Long votes_down) {
        this.votes_down = votes_down;
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

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }
}
