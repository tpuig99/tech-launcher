package ar.edu.itba.paw.webapp.form.frameworks;

import javax.validation.constraints.NotNull;

public class RatingForm {
    @NotNull
    private long frameworkId;
    @NotNull
    private int rating;

    public long getFrameworkId() {
        return frameworkId;
    }

    public void setFrameworkId(long frameworkId) {
        this.frameworkId = frameworkId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
