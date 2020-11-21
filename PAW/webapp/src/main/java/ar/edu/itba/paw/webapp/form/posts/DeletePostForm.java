package ar.edu.itba.paw.webapp.form.posts;

import javax.validation.constraints.NotNull;

public class DeletePostForm {
    @NotNull
    private long postIdx;

    public long getPostIdx() {
        return postIdx;
    }

    public void setPostIdx(long postIdx) {
        this.postIdx = postIdx;
    }
}
