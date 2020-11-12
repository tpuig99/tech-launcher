package ar.edu.itba.paw.webapp.form.posts;

import ar.edu.itba.paw.models.PostTag;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@NotEmptyTags
public class AddPostForm {

    @NotEmpty
    @Size(min = 3, max = 200)
    private String title;

    @NotEmpty
    @Size(max = 5000)
    private String description;

    private List<String> names;

    private List<String> categories;

    private List<String> types;


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

    public List<String> getNames() {
        return names;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }
}
