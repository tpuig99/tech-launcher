package ar.edu.itba.paw.webapp.form.framework;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ReportCommentForm {
    @NotNull
    @NotEmpty
    @Size(min=4, max=500)
    private String reportCommentDescription;

    private long reportCommentId;

    private long reportCommentFrameworkId;

    public String getReportCommentDescription() {
        return reportCommentDescription;
    }

    public void setReportCommentDescription(String reportCommentDescription) {
        this.reportCommentDescription = reportCommentDescription;
    }

    public long getReportCommentId() {
        return reportCommentId;
    }

    public void setReportCommentId(long reportCommentId) {
        this.reportCommentId = reportCommentId;
    }

    public long getReportCommentFrameworkId() {
        return reportCommentFrameworkId;
    }

    public void setReportCommentFrameworkId(long reportCommentFrameworkId) {
        this.reportCommentFrameworkId = reportCommentFrameworkId;
    }
}
