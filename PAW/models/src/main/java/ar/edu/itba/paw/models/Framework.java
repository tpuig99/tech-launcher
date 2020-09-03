package ar.edu.itba.paw.models;

public class Framework {
    private long id;
    private String frameworkname;
    private String category;
    private String description;

    public Framework(long id, String name, String category, String description) {
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
        return category;
    }

    public String getDescription() {
        return description;
    }
}
