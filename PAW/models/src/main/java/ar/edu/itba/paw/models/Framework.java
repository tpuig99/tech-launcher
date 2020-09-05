package ar.edu.itba.paw.models;

import java.util.List;

public class Framework {
    private long id;
    private String frameworkname;
    private FrameworkCategories category;
    private String description;

    public Framework(long id, String name, FrameworkCategories category, String description) {
        this.id = id;
        this.frameworkname = name;
        this.category = category;
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public String getFrameworkname() {
        return frameworkname;
    }

    public String getCategory() {
        return category.name();
    }

    public String getDescription() {
        return description;
    }

}
