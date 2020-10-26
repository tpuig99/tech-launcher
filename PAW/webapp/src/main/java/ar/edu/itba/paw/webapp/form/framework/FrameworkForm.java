package ar.edu.itba.paw.webapp.form.framework;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@FrameworkName
@Multipart
public class FrameworkForm {
    @NotNull
    @NotEmpty
    @Size(min = 3, max = 50)
    @Pattern(regexp = "[a-zA-Z0-9 -]+")
    private String frameworkName;

    @NotNull
    @NotEmpty
    @Size(max = 50)
    private String category;

    @NotNull
    @NotEmpty
    @Size(max = 100)
    private String type;

    @NotNull
    @NotEmpty
    @Size(max = 500)
    private String description;

    @NotNull
    @NotEmpty
    @Size(max = 5000)
    private String introduction;
    private MultipartFile picture;
    private Long frameworkId;

    public String getFrameworkName() {
        return frameworkName;
    }

    public void setFrameworkName(String frameworkName) {
        this.frameworkName = frameworkName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public MultipartFile getPicture() {
        return picture;
    }

    public void setPicture(MultipartFile picture) {
        this.picture = picture;
    }

    public Long getFrameworkId() {
        return frameworkId;
    }

    public void setFrameworkId(long frameworkId) {
        this.frameworkId = frameworkId;
    }
}
