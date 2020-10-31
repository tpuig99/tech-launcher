package ar.edu.itba.paw.models;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.*;

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
    @Type(type = "org.hibernate.type.BinaryType")
    private byte[] picture;

    /* Relationships */

    /* References other relation mapped in Comment */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "framework")
    private List<Comment> comments;

    /* References other relation mapped in Content */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "framework")
    private List<Content> contents;

    /* References other relation mapped in Framework Votes */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "framework")
    private List<FrameworkVote> frameworkVotes;

    /* References other relation mapped in Verify User */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "framework")
    private List<VerifyUser> verifyUsers;

    /* Transient attributes loaded on Post Load */

    @Transient
    private String contentType;

    @Transient
    private String base64image;

    @Transient
    private double stars = 0;

    @Transient
    private int votesCant;

    @Transient
    private Timestamp lastComment;

    @Transient
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

    @PostLoad
    public void postLoad() {
        if(picture!=null) {
            try {
                contentType = URLConnection.guessContentTypeFromStream(new ByteArrayInputStream(picture));
            } catch (IOException e) {
                contentType = "";
            }

            byte[] encodedByteArray = Base64.getEncoder().encode(picture);
            base64image = new String(encodedByteArray, StandardCharsets.UTF_8);
        }
        if(frameworkVotes != null && !frameworkVotes.isEmpty()) {
            double rating = 0;
            for (FrameworkVote vote : frameworkVotes) {
                rating += vote.getStars();
            }
            stars = rating / frameworkVotes.size();
        }
        votesCant = frameworkVotes.size();
        if(comments != null && !comments.isEmpty()) {
            lastComment = Collections.max(comments, Comparator.comparingDouble((x) -> x.getTimestamp().getTime())).getTimestamp();
            commentsAmount = comments.size();
        } else
            commentsAmount = 0;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
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

    public double getStars() {
        return stars;
    }
    public String getStarsFormated(){
        return String.format("%.2f", stars);
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

    public List<Content> getContentByType(ContentTypes type){
        List<Content> content = new ArrayList<>();
        for (Content ct: contents) {
            if(ct.getType().equals(type))
                content.add(ct);
        }
        return content;
    }

    public long getBooksAmount() {
        return getContentAmount(ContentTypes.book);
    }

    public long getCoursesAmount() {
        return getContentAmount(ContentTypes.course);
    }

    public long getTutorialsAmount() {
        return getContentAmount(ContentTypes.tutorial);
    }

    public long getContentAmount(ContentTypes type) {
        return contents.stream().filter((x) -> x.getType() == type).count();
    }
}
