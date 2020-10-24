package ar.edu.itba.paw.models;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "comments_comment_id_seq")
    @SequenceGenerator(sequenceName = "comments_comment_id_seq", name = "comments_comment_id_seq", allocationSize = 1)
    @Column(name = "comment_id")
    private long commentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "framework_id",nullable = false)
    private Framework framework;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "description", nullable = false, length = 500)
    private String description;


    private long votesUp;
    private long votesDown;

    @Column(name = "tstamp", nullable = false)
    private Timestamp timestamp;

    @Column(name = "reference")
    private Long reference;

    private String frameworkName;
    private String userName;
    private FrameworkCategories category;
    private boolean isVerify;
    private boolean isAdmin;
    private Integer userAuthVote;
    private List<String> reportersNames = new ArrayList<>();

    public Comment(long commentId, Framework framework, User user, String description, long votesUp, long votesDown, Timestamp timestamp, Long reference, String frameworkName, String userName, FrameworkCategories category, boolean isVerify, boolean isAdmin) {
        this.commentId = commentId;
        this.framework = framework;
        this.user = user;
        this.description = description;
        this.votesUp = votesUp;
        this.votesDown = votesDown;
        this.timestamp = timestamp;
        this.reference = reference;
        this.frameworkName = frameworkName;
        this.userName = userName;
        this.category = category;
        this.isVerify = isVerify;
        this.isAdmin = isAdmin;
    }
    public Comment(long commentId, long frameworkId, long userId, String description, long votesUp, long votesDown, Timestamp timestamp, Long reference, String frameworkName, String userName, FrameworkCategories category, boolean isVerify, boolean isAdmin, Integer userAuthVote) {
        this.commentId = commentId;
        this.frameworkId = frameworkId;
        this.userId = userId;
        this.description = description;
        this.votesUp = votesUp;
        this.votesDown = votesDown;
        this.timestamp = timestamp;
        this.reference = reference;
        this.frameworkName = frameworkName;
        this.userName = userName;
        this.category = category;
        this.isVerify = isVerify;
        this.isAdmin = isAdmin;
        this.userAuthVote = userAuthVote;
    }

    public Comment() {

    }


    public long getCommentId() {
        return commentId;
    }

    public long getFrameworkId() {
        return frameworkId;
    }

    public long getUserId() {
        return userId;
    }

    public String getDescription() {
        return description;
    }

    public long getVotesUp() {
        return votesUp;
    }

    public long getVotesDown() {
        return votesDown;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public Long getReference() {
        return reference;
    }

    public String getFrameworkName() {
        return frameworkName;
    }

    public String getUserName() {
        return userName;
    }

    public String getCategory() {
        return category.getNameCat();
    }

    public FrameworkCategories getEnumCategory() {
        return category;
    }

    public boolean isVerify() {
        return isVerify;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public int getUserAuthVote() {
        return userAuthVote;
    }
    public boolean hasUserAuthVote(){
        if(userAuthVote == null)
            return false;
        return userAuthVote == 0 ? false : true;
    }

    public void addReporter(String name) {
        reportersNames.add(name);
    }
    public boolean isReporter(String name){
        for (String rn:reportersNames) {
            if(rn.equals(name))
                return true;
        }
        return false;
    }
}
