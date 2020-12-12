package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Framework;

import java.util.Date;
import java.util.List;

public class FrameworkDTO {
    private String techName;
    private String description;
    private String introduction;
    private String category;
    private String author;
    private String type;
    private Date date;
    private byte[] picture;
    private Integer votes_cant;
    private Double stars;
    private Integer comments_amount;
    private List<CommentDTO> comments;
    private List<ContentDTO> book;
    private List<ContentDTO> tutorial;
    private List<ContentDTO> course;


    public static FrameworkDTO fromFramework(Framework framework) {
        final FrameworkDTO dto = new FrameworkDTO();
        dto.techName = framework.getName();
        dto.description = framework.getDescription();
        dto.introduction = framework.getIntroduction();
        dto.category = framework.getCategory().name();
        dto.author = framework.getAuthor().getUsername();
        dto.type = framework.getType().name();
        dto.date = framework.getPublishDate();
        dto.picture = framework.getPicture();
        dto.votes_cant = framework.getVotesCant();
        dto.stars = framework.getStars();
        dto.comments_amount = framework.getCommentsAmount();
        return dto;
    }

    public String getTechName() {
        return techName;
    }

    public void setTechName(String techName) {
        this.techName = techName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public Integer getVotes_cant() {
        return votes_cant;
    }

    public void setVotes_cant(Integer votes_cant) {
        this.votes_cant = votes_cant;
    }

    public Double getStars() {
        return stars;
    }

    public void setStars(Double stars) {
        this.stars = stars;
    }

    public Integer getComments_amount() {
        return comments_amount;
    }

    public void setComments_amount(Integer comments_amount) {
        this.comments_amount = comments_amount;
    }

    public List<CommentDTO> getComments() {
        return comments;
    }

    public void setComments(List<CommentDTO> comments) {
        this.comments = comments;
    }

    public List<ContentDTO> getBook() {
        return book;
    }

    public void setBook(List<ContentDTO> book) {
        this.book = book;
    }

    public List<ContentDTO> getTutorial() {
        return tutorial;
    }

    public void setTutorial(List<ContentDTO> tutorial) {
        this.tutorial = tutorial;
    }

    public List<ContentDTO> getCourse() {
        return course;
    }

    public void setCourse(List<ContentDTO> course) {
        this.course = course;
    }
}
