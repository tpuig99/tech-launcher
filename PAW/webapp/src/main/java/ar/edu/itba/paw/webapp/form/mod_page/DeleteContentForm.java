package ar.edu.itba.paw.webapp.form.mod_page;

import javax.validation.constraints.NotNull;

public class DeleteContentForm {

    private long deleteContentId;

    public long getDeleteContentId() {
        return deleteContentId;
    }

    public void setDeleteContentId(long deleteContentId) {
        this.deleteContentId = deleteContentId;
    }
}
