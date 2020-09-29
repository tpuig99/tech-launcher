package ar.edu.itba.paw.webapp.form;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class FrameworkForm {
    @NotNull
    @NotEmpty
    @Size(max = 50)
    @FrameworkName
    @Pattern(regexp = "[a-zA-Z0-9]+")
    private String frameworkName;
    @NotNull
    @NotEmpty
    @Size(max = 50)
    @Pattern(regexp = "[a-zA-Z0-9]+")
    private String Category;
    @NotNull
    @NotEmpty
    @Size(max = 100)
    @Pattern(regexp = "[a-zA-Z0-9]+")
    private String Type;
    @NotNull
    @NotEmpty
    @Size(max = 500)
    @Pattern(regexp = "[a-zA-Z0-9]+")
    private String Description;
    @NotNull
    @NotEmpty
    @Size(max = 5000)
    @Pattern(regexp = "[a-zA-Z0-9]+")
    private String introduction;

    public String getFrameworkName() {
        return frameworkName;
    }

    public void setFrameworkName(String frameworkName) {
        this.frameworkName = frameworkName;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }
}
