package ar.edu.itba.paw.webapp.form.framework;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ReportForm {
    @NotNull
    @NotEmpty
    @Size(min=4, max=500)
    private String description;

    private long id;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
