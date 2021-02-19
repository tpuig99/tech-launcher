package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Comment;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class CommentAddDTO {

    @NotNull
    @Size(max=500)
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
