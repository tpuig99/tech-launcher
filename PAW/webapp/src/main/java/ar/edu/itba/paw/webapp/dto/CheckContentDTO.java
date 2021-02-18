package ar.edu.itba.paw.webapp.dto;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CheckContentDTO {

    @NotNull
    @NotEmpty
    @Size(min=4, max=40)
    private String title;

    @NotNull
    @NotEmpty
    private String type;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
