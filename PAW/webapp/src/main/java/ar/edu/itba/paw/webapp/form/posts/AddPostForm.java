package ar.edu.itba.paw.webapp.form.posts;

import ar.edu.itba.paw.models.PostTag;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

public class AddPostForm {
    @NotEmpty
    @Size(min = 3, max = 50)
    @Pattern(regexp = "[a-zA-Z0-9 -]+")
    String title;

    @NotNull
    @NotEmpty
    @Size(max = 5000)
    String description;

    @NotNull
    @NotEmpty
    String tag;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
