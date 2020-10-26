package ar.edu.itba.paw.models;

import javax.persistence.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "frameworks")
public class Framework {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "frameworks_framework_id_seq")
    @SequenceGenerator(sequenceName = "frameworks_framework_id_seq", name = "frameworks_framework_id_seq", allocationSize = 1)
    @Column(name = "framework_id")
    private long id;

    @Column(name = "framework_name", length = 50)
    private String name;

    @Column(name = "category", length = 50)
    @Enumerated(EnumType.STRING)
    private FrameworkCategories category;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "introduction", length = 1500)
    private String introduction;

    @Column(name = "type", length = 100)
    @Enumerated(EnumType.STRING)
    private FrameworkType type;

    @Column(name = "logo", length = 150)
    private String logo;

    /* References other relation mapped in User */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author", nullable = false)
    private User author;

    @Column(name = "date")
    private Timestamp publishDate;

    @Lob
    private byte[] picture;
    private String contentType;
    private String base64image;

    /* References other relation mapped in Comment */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "framework")
    @JoinColumn(name = "framework_id")
    private List<Comment> comments;
    private Timestamp lastComment;
    private int commentsAmount;

    /* References other relation mapped in Content */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "framework")
    @JoinColumn(name = "framework_id")
    private List<Content> contents;

    /* References other relation mapped in Framework Votes */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "framework")
    @JoinColumn(name = "framework_id")
    private List<FrameworkVote> frameworkVotes;
    private double stars;
    private int votesCant;

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

    public Framework(User author, String name, FrameworkCategories category, String description, String introduction,
                     String logo, FrameworkType type, Timestamp publishDate, byte[] picture) {
        this.name = name;
        this.category = category;
        this.description = description;
        this.introduction = introduction;
        this.logo = logo;
        this.type = type;
        this.author = author;
        this.publishDate = publishDate;
        this.picture = picture;
    }

    public Framework(){
        /* Empty constructor */
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FrameworkCategories getCategory() {
        return category;
    }

    public void setCategory(FrameworkCategories category) {
        this.category = category;
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

    public FrameworkType getType() {
        return type;
    }

    public void setType(FrameworkType type) {
        this.type = type;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Timestamp getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Timestamp publishDate) {
        this.publishDate = publishDate;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;

        if (picture != null) {
            byte[] encodedByteArray = Base64.getEncoder().encode(picture);
            setBase64image(new String(encodedByteArray, StandardCharsets.UTF_8));
            try {
                setContentType(URLConnection.guessContentTypeFromStream(new ByteArrayInputStream(picture)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getBase64image() {
        return base64image;
    }

    public void setBase64image(String base64image) {
        this.base64image = base64image;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public Timestamp getLastComment() {
        return lastComment;
    }

    public void setLastComment(Timestamp lastComment) {
        this.lastComment = lastComment;
    }

    public int getCommentsAmount() {
        return commentsAmount;
    }

    public void setCommentsAmount(int commentsAmount) {
        this.commentsAmount = commentsAmount;
    }

    public List<Content> getContents() {
        return contents;
    }

    public void setContents(List<Content> contents) {
        this.contents = contents;
    }

    public List<FrameworkVote> getFrameworkVotes() {
        return frameworkVotes;
    }

    public void setFrameworkVotes(List<FrameworkVote> frameworkVotes) {
        this.frameworkVotes = frameworkVotes;
    }

    public double getStars() {
        return stars;
    }

    public void setStars(double stars) {
        this.stars = stars;
    }

    public int getVotesCant() {
        return votesCant;
    }

    public void setVotesCant(int votesCant) {
        this.votesCant = votesCant;
    }
}
