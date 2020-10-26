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

    @Column(name = "description", nullable = false, length = 500)
    private String description;

    private long votesUp;
    private long votesDown;

    @Column(name = "tstamp", nullable = false)
    private Timestamp timestamp;

    @Column(name = "reference")
    private Long reference;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "framework_id",nullable = false)
    private Framework framework;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /*this refers to the other relation mapped in CommentVote*/
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "comment")
    @JoinColumn(name = "comment_id")
    private List<CommentVote> commentVotes;

    /*this refers to the other relation mapped in ReportComment*/
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "comment")
    @JoinColumn(name = "comment_id")
    private List<ReportComment> reports;

    /*this refers to the other relation mapped in VerifyUser*/
    @OneToOne(fetch = FetchType.LAZY,mappedBy = "comment")
    @JoinColumn(name = "comment_id")
    private VerifyUser verifyUser;

    private String frameworkName;
    private String userName;
    private FrameworkCategories category;
    private boolean isVerify;
    private boolean isAdmin;
    private Integer userAuthVote;
    private List<String> reportersNames = new ArrayList<>();

    public Comment(Framework framework, User user, String description, long votesUp, long votesDown, Timestamp timestamp, Long reference) {
        this.framework = framework;
        this.user = user;
        this.description = description;
        this.votesUp = votesUp;
        this.votesDown = votesDown;
        this.timestamp = timestamp;
        this.reference = reference;
        this.frameworkName = framework.getName();
        this.userName = user.getUsername();
        this.category = framework.getCategory();
        this.isVerify = user.isVerify();
        this.isAdmin = user.isAdmin();
    }
    public Comment(Framework framework, User user, String description, long votesUp, long votesDown, Timestamp timestamp, Long reference, FrameworkCategories category, Integer userAuthVote) {
        this.framework = framework;
        this.user = user;
        this.description = description;
        this.votesUp = votesUp;
        this.votesDown = votesDown;
        this.timestamp = timestamp;
        this.reference = reference;
        this.frameworkName = framework.getName();
        this.userName = user.getUsername();
        this.category = category;
        this.isVerify = user.isVerify();
        this.isAdmin = user.isAdmin();
        this.userAuthVote = userAuthVote;
    }

    public Comment() {
    }


    public long getCommentId() {
        return commentId;
    }

    public long getFrameworkId() {
        return framework.getId();
    }

    public long getUserId() {
        return user.getId();
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

    public Framework getFramework() {
        return framework;
    }

    public void setFramework(Framework framework) {
        this.framework = framework;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}

