package ar.edu.itba.paw.webapp.form.framework;

import ar.edu.itba.paw.models.Framework;
import ar.edu.itba.paw.models.FrameworkCategories;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ReportForm {
    @NotNull
    @NotEmpty
    @Size(min=4, max=500)
    private String description;

    private long id;

    private long reportFrameworkId;


    public long getReportFrameworkId() {
        return reportFrameworkId;
    }

    public void setReportFrameworkId(long reportFrameworkId) {
        this.reportFrameworkId = reportFrameworkId;
    }

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
