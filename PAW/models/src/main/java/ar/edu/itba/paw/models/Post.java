package ar.edu.itba.paw.models;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "posts_post_id_seq")
    @SequenceGenerator(sequenceName = "posts_post_id_seq", name = "posts_post_id_seq", allocationSize = 1)
    @Column(name = "post_id")
    private long postId;

    @Column(name = "description", nullable = false, length = 5000)
    private String description;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "tstamp", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /*this refers to the other relation mapped in PostVote */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<PostVote> postVotes;

    /*this refers to the other relation mapped in PostComment */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<PostComment> postComments;

    /*this refers to the other relation mapped in PostTag */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<PostTag> postTags;

    public Post(){
        //For Hibernate
    }

    public long getPostId() {
        return postId;
    }

    public void setPostId(long postId) {
        this.postId = postId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<PostVote> getPostVotes() {
        return postVotes;
    }


    public List<PostComment> getPostComments() {
        return postComments;
    }


    public List<PostTag> getPostTags() {
        return postTags;
    }

}
