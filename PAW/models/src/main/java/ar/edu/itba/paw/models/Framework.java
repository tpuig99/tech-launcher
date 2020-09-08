package ar.edu.itba.paw.models;

import java.util.List;

public class Framework {
    private long id;
    private String name;
    private FrameworkCategories category;
    private String description;
    private String introduction;
    private String logo;
    private double stars;
    private int votesCant;

    public Framework(long id, String name, FrameworkCategories category, String description, String introduction, String logo) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.description = description;
        this.introduction = introduction;
        this.logo = logo;
    }

    public int getVotesCant() {
        return votesCant;
    }

    public void setVotesCant(int votesCant) {
        this.votesCant = votesCant;
    }

    @Override
    public String toString() {
        return "Framework{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category=" + category +
                ", description='" + description + '\'' +
                ", introduction='" + introduction + '\'' +
                ", logo='" + logo + '\'' +
                ", stars=" + stars +
                ", votesCant=" + votesCant +
                '}';
    }

    public double getStars() {
        return stars;
    }

    public void setStars(double stars) {
        this.stars = stars;
    }


    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category.getNameCat();
    }
    public FrameworkCategories getFrameCategory(){
        return category;
    }

    public String getDescription() {
        return description;
    }

    public String getIntroduction() {
        return introduction;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
