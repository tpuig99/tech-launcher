package ar.edu.itba.paw.webapp.form.framework;

import javax.validation.constraints.NotNull;

public class DeleteFrameworkForm {
    @NotNull
    private long frameworkIdx;

    public long getFrameworkIdx() {
        return frameworkIdx;
    }

    public void setFrameworkIdx(long frameworkIdx) {
        this.frameworkIdx = frameworkIdx;
    }
}
