package ar.edu.itba.paw.webapp.form.frameworks;

import javax.validation.constraints.NotNull;

public class DeleteContentForm {
    @NotNull
    private long deleteContentFrameworkId;
    @NotNull
    private Long deleteContentId;

    public long getDeleteContentFrameworkId() {
        return deleteContentFrameworkId;
    }

    public void setDeleteContentFrameworkId(long deleteContentFrameworkId) {
        this.deleteContentFrameworkId = deleteContentFrameworkId;
    }

    public Long getDeleteContentId() {
        return deleteContentId;
    }

    public void setDeleteContentId(Long deleteContentId) {
        this.deleteContentId = deleteContentId;
    }
}
