package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ContentForm {

    @NotNull
    @Size(min=4, max=40)
    private String title;
    @NotNull
    private String link;

    private String type;

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getType() {
        return type;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setType(String type) {
        this.type = type;
    }
}
