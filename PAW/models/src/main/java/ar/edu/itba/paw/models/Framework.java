package ar.edu.itba.paw.models;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

public class Framework {
    private long id;
    private String name;
    private FrameworkCategories category;
    private String description;
    private String introduction;
    private FrameworkType type;
    private String logo;
    private double stars;
    private int votesCant;
    private String author;
    private Timestamp publish_date;
    private Timestamp lastComment;
    private int commentsAmount;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Framework framework = (Framework) o;
        return id == framework.id &&
                name.equals(framework.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    public Framework(long id, String name, FrameworkCategories category, String description, String introduction, String logo, double stars, int votesCant,FrameworkType type,String author,Timestamp publish_date,Timestamp lastComment,int commentsAmount) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.description = description;
        this.introduction = introduction;
        this.logo = logo;
        this.stars = stars;
        this.votesCant = votesCant;
        this.type = type;
        this.author = author;
        this.publish_date = publish_date;
        this.lastComment = lastComment;
        this.commentsAmount = commentsAmount;
    }

    public int getVotesCant() {
        return votesCant;
    }

    public double getStars(){
        return stars;
    }

    public String getStarsFormated() {
        return String.format("%.2f", stars);
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

    public String getType(){
        return type.getType();
    }
    public FrameworkType getFrameType() {
        return type;
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

    public String getAuthor() {
        return author;
    }

    public Timestamp getPublish_date() {
        return publish_date;
    }

    public Timestamp getLastComment() {
        return lastComment;
    }

    public int getCommentsAmount() {
        return commentsAmount;
    }
}
