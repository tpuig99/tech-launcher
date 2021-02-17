package ar.edu.itba.paw.webapp.dto;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

public class FrameworkAddDTO {

    @NotNull
    @NotEmpty
    @Size(min = 1, max = 50)
    @Pattern(regexp = "[a-zA-Z0-9 -+#*]+")
    private String techName;

    @NotNull
    @NotEmpty
    @Size(min = 1, max = 500)
    private String description;

    @NotNull
    @NotEmpty
    @Size(min = 1, max = 5000)
    private String introduction;

    @NotNull
    @NotEmpty
    @Size(min = 1, max = 50)
    private String category;

    @NotNull
    @NotEmpty
    @Size(min = 1, max = 100)
    private String type;
    private byte[] picture;

    public String getTechName() {
        return techName;
    }

    public void setTechName(String techName) {
        this.techName = techName;
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

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }
}
