package ar.edu.itba.paw.models;

import java.net.URL;
import java.sql.Timestamp;

public class Content {
    private long contentId;
    private long frameworkId;
    private long userId;
    private String title;
    private Timestamp timestamp;
    private String link;
    private ContentTypes type;
    private int votesUp;
    private int votesDown;
    private String frameworkName;
    private FrameworkCategories category;
    private Integer userAuthVote;
    private boolean isVerify;
    private boolean isAdmin;

    public Content(long contentId, long frameworkId, long userId, String title, int votesUp, int votesDown, Timestamp timestamp, String link, ContentTypes type, String frameworkName, FrameworkCategories category, boolean isVerify, boolean isAdmin, Integer userAuthVote) {
        this.contentId = contentId;
        this.frameworkId = frameworkId;
        this.userId = userId;
        this.title = title;
        this.votesUp = votesUp;
        this.votesDown = votesDown;
        this.timestamp = timestamp;
        this.link = link;
        this.type = type;
        this.frameworkName = frameworkName;
        this.category = category;
        this.userAuthVote = userAuthVote;
        this.isVerify = isVerify;
        this.isAdmin = isAdmin;
    }
    public Content(long contentId, long frameworkId, long userId, String title, int votesUp, int votesDown, Timestamp timestamp, String link, ContentTypes type, String frameworkName, FrameworkCategories category, boolean isVerify, boolean isAdmin) {
        this.contentId = contentId;
        this.frameworkId = frameworkId;
        this.userId = userId;
        this.title = title;
        this.votesUp = votesUp;
        this.votesDown = votesDown;
        this.timestamp = timestamp;
        this.link = link;
        this.type = type;
        this.frameworkName = frameworkName;
        this.category = category;
        this.isVerify = isVerify;
        this.isAdmin = isAdmin;
    }


    public long getContentId() {
        return contentId;
    }

    public long getFrameworkId() {
        return frameworkId;
    }

    public long getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public int getVotesUp() {
        return votesUp;
    }

    public int getVotesDown() {
        return votesDown;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public String getLink() {
        return link;
    }

    public ContentTypes getType() {
        return type;
    }

    public String getFrameworkName() {
        return frameworkName;
    }

    public FrameworkCategories getCategory() {
        return category;
    }

    public Integer getUserAuthVote() {
        return userAuthVote;
    }

    public boolean isVerify() {
        return isVerify;
    }

    public boolean isAdmin() {
        return isAdmin;
    }
    public boolean hasUserAuthVote(){
        if(userAuthVote == null)
            return false;
        return userAuthVote == 0 ? false : true;
    }
}
