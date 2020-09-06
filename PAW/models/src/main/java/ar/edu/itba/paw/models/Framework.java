package ar.edu.itba.paw.models;

import java.util.List;

public class Framework {
    private long id;
    private String name;
    private FrameworkCategories category;
    private String description;
    private double stars;

    @Override
    public String toString() {
        return "Framework{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category=" + category +
                ", description='" + description + '\'' +
                ", stars=" + stars +
                '}';
    }

    public double getStars() {
        return stars;
    }

    public void setStars(double stars) {
        this.stars = stars;
    }

    public Framework(long id, String name, FrameworkCategories category, String description) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category.name();
    }

    public String getDescription() {
        return description;
    }

}
