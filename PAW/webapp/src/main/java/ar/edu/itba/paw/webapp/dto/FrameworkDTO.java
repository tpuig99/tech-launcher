package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Framework;

import javax.ws.rs.core.UriInfo;

public class FrameworkDTO {
    private String name;
    private String description;
    private String introduction;
    private String category;
    private String author;
    private String authorLocation;
    private String type;
    private String date;
    private Integer votesCant;
    private Double stars;
    private Integer commentsAmount;
    private String comments;
    private String books;
    private String tutorials;
    private String courses;
    private String location;
    private String picture;
    private String relatedPosts;
    private Boolean hasPicture;
    private String competitors;
    private int loggedStars;

    public static FrameworkDTO fromFramework(Framework framework, UriInfo uriInfo) {
        final FrameworkDTO dto = new FrameworkDTO();
        dto.name = framework.getName();
        dto.description = framework.getDescription();
        dto.introduction = framework.getIntroduction();
        dto.category = framework.getCategory().name();
        dto.author = framework.getAuthor().getUsername();
        dto.authorLocation = "users/"+framework.getAuthor().getId();
        dto.type = framework.getType().name();
        dto.date = framework.getPublishDate().toLocaleString();
        dto.votesCant = framework.getVotesCant();
        dto.stars = framework.getStars();
        dto.commentsAmount = framework.getCommentsAmount();
        dto.hasPicture = framework.getPicture() != null;
        dto.picture = "techs/"+framework.getId()+"/image";
        dto.comments = "techs/"+framework.getId()+"/comment";
        dto.books = "techs/"+framework.getId()+"/content?type=book";
        dto.courses = "techs/"+framework.getId()+"/content?type=course";
        dto.tutorials = "techs/"+framework.getId()+"/content?type=tutorial";
        dto.relatedPosts = "explore?toSearch="+framework.getName()+"&isPost=true";
        dto.competitors = "techs/"+framework.getId()+"/competitors";
        return dto;
    }

    public static FrameworkDTO fromExtern(Framework framework,UriInfo uriInfo) {
        final FrameworkDTO dto = new FrameworkDTO();
        dto.name = framework.getName();
        dto.hasPicture = framework.getPicture() != null;
        dto.picture = "techs/"+framework.getId()+"/image";
        dto.location = "techs/"+framework.getId();
        dto.stars = framework.getStars();
        return dto;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getIntroduction() {
        return introduction;
    }

    public String getCategory() {
        return category;
    }

    public String getAuthor() {
        return author;
    }

    public String getAuthorLocation() {
        return authorLocation;
    }

    public String getType() {
        return type;
    }

    public String getDate() {
        return date;
    }

    public String getPicture() {
        return picture;
    }

    public Integer getVotesCant() {
        return votesCant;
    }

    public Double getStars() {
        return stars;
    }

    public Integer getCommentsAmount() {
        return commentsAmount;
    }

    public String getComments() {
        return comments;
    }

    public String getBooks() {
        return books;
    }

    public String getTutorials() {
        return tutorials;
    }

    public String getCourses() {
        return courses;
    }

    public String getLocation() {
        return location;
    }
    public void setName(String techName) {
        this.name = techName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setAuthorLocation(String authorLocation) {
        this.authorLocation = authorLocation;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setVotesCant(Integer votesCant) {
        this.votesCant = votesCant;
    }

    public void setStars(Double stars) {
        this.stars = stars;
    }

    public void setCommentsAmount(Integer commentsAmount) {
        this.commentsAmount = commentsAmount;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public void setBooks(String books) {
        this.books = books;
    }

    public void setTutorials(String tutorials) {
        this.tutorials = tutorials;
    }

    public void setCourses(String courses) {
        this.courses = courses;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Boolean getHasPicture() {
        return hasPicture;
    }

    public void setHasPicture(Boolean hasPicture) {
        this.hasPicture = hasPicture;
    }

    public String getRelatedPosts() {
        return relatedPosts;
    }

    public void setRelatedPosts(String relatedPosts) {
        this.relatedPosts = relatedPosts;
    }

    public String getCompetitors() {
        return competitors;
    }

    public void setCompetitors(String competitors) {
        this.competitors = competitors;
    }

    public int getLoggedStars() {
        return loggedStars;
    }

    public void setLoggedStars(int loggedStars) {
        this.loggedStars = loggedStars;
    }
}
