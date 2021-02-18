package ar.edu.itba.paw.webapp.dto;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class PostCommentAddDTO {

    @NotNull
    @NotEmpty
    @Size(min = 1, max = 500)
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
