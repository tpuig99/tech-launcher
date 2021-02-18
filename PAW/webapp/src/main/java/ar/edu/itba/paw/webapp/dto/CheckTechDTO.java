package ar.edu.itba.paw.webapp.dto;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class CheckTechDTO {

    @NotNull
    @NotEmpty
    @Size(min = 1, max = 50)
    @Pattern(regexp = "[a-zA-Z0-9 -+#*]+")
    private String name;

    private Integer id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
