package ar.edu.itba.paw.models;

import jdk.jfr.ContentType;

import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Content {
    private Long contentId;
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
    private String userName;
    private Integer userAuthVote;
    private boolean isVerify;
    private boolean isAdmin;
    private List<String> reportersNames = new ArrayList<>();

    public Content(){

    }

    public Content(long contentId, long frameworkId, long userId, String title, int votesUp, int votesDown, Timestamp timestamp, String link, ContentTypes type, String frameworkName, FrameworkCategories category, boolean isVerify, boolean isAdmin, String username,Integer userAuthVote) {
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
        this.userName = username;
    }
    public Content(long contentId, long frameworkId, long userId, String title, int votesUp, int votesDown, Timestamp timestamp, String link, ContentTypes type, String frameworkName, FrameworkCategories category, boolean isVerify, boolean isAdmin,String userName) {
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
        this.userName = userName;
    }

    public Content(long frameworkId, long userId, String title, String link, ContentTypes type){
        this.frameworkId = frameworkId;
        this.userId = userId;
        this.title = title;
        this.link = link;
        this.type = type;
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

    public String getUserName() {
        return userName;
    }

    public List<String> getReportersNames() {
        return reportersNames;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setType(ContentTypes type) {
        this.type = type;
    }
}
