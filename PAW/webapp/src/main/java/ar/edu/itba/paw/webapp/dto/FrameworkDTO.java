package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Framework;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.awt.*;
import java.util.Date;
import java.util.List;

public class FrameworkDTO {
    private String name;
    private String description;
    private String introduction;
    private String category;
    private String author;
    private String author_location;
    private String type;
    private Date date;
    private byte[] picture;
    private Integer votes_cant;
    private Double stars;
    private Integer comments_amount;
    private String comments;
    private String book;
    private String tutorial;
    private String course;
    private String location;


    public static FrameworkDTO fromFramework(Framework framework, UriInfo uriInfo) {
        final FrameworkDTO dto = new FrameworkDTO();
        dto.name = framework.getName();
        dto.description = framework.getDescription();
        dto.introduction = framework.getIntroduction();
        dto.category = framework.getCategory().name();
        dto.author = framework.getAuthor().getUsername();
        dto.author_location = uriInfo.getBaseUriBuilder().path("users/"+framework.getAuthor().getId()).build().toString();
        dto.type = framework.getType().name();
        dto.date = framework.getPublishDate();
        dto.picture = framework.getPicture();
        dto.votes_cant = framework.getVotesCant();
        dto.stars = framework.getStars();
        dto.comments_amount = framework.getCommentsAmount();
        dto.comments = uriInfo.getAbsolutePathBuilder().path("comments").build().toString();
        dto.book = uriInfo.getAbsolutePathBuilder().path("content").build().toString()+"?type=book";
        dto.course = uriInfo.getAbsolutePathBuilder().path("content").build().toString()+"?type=course";
        dto.tutorial = uriInfo.getAbsolutePathBuilder().path("content").build().toString()+"?type=tutorial";
        return dto;
    }
    public static FrameworkDTO fromExtern(Framework framework,UriInfo uriInfo) {
        final FrameworkDTO dto = new FrameworkDTO();
        dto.name = framework.getName();
        dto.picture = framework.getPicture();
        dto.location = uriInfo.getBaseUriBuilder().path("techs/"+framework.getId()).build().toString();
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

    public String getAuthor_location() {
        return author_location;
    }

    public String getType() {
        return type;
    }

    public Date getDate() {
        return date;
    }

    public byte[] getPicture() {
        return picture;
    }

    public Integer getVotes_cant() {
        return votes_cant;
    }

    public Double getStars() {
        return stars;
    }

    public Integer getComments_amount() {
        return comments_amount;
    }

    public String getComments() {
        return comments;
    }

    public String getBook() {
        return book;
    }

    public String getTutorial() {
        return tutorial;
    }

    public String getCourse() {
        return course;
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

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setAuthor_location(String author_location) {
        this.author_location = author_location;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setVotes_cant(Integer votes_cant) {
        this.votes_cant = votes_cant;
    }

    public void setStars(Double stars) {
        this.stars = stars;
    }

    public void setComments_amount(Integer comments_amount) {
        this.comments_amount = comments_amount;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public void setBook(String book) {
        this.book = book;
    }

    public void setTutorial(String tutorial) {
        this.tutorial = tutorial;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
