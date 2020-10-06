package ar.edu.itba.paw.models;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Comment {
    private long commentId;
    private long frameworkId;
    private long userId;
    private String description;
    private long votesUp;
    private long votesDown;
    private Timestamp timestamp;
    private Long reference;
    private String frameworkName;
    private String userName;
    private FrameworkCategories category;
    private boolean isVerify;
    private boolean isAdmin;
    private Integer userAuthVote;
    private List<String> reportersNames = new ArrayList<>();

    public Comment(long commentId, long frameworkId, long userId, String description, long votesUp, long votesDown, Timestamp timestamp, Long reference, String frameworkName, String userName, FrameworkCategories category, boolean isVerify, boolean isAdmin) {
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
