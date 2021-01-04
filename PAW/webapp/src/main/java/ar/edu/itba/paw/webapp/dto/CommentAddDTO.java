package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Comment;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class CommentAddDTO {
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
