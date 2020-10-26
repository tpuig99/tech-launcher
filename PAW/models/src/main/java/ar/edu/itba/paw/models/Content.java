package ar.edu.itba.paw.models;

import jdk.jfr.ContentType;

import javax.persistence.*;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "content")
public class Content {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "content_content_id_seq")
    @SequenceGenerator(sequenceName = "content_content_id_seq", name = "content_content_id_seq", allocationSize = 1)
    @Column(name = "content_id")
    private Long contentId;

    @Column(name = "framework_id")
    private long frameworkId;

    @Column(name = "user_id")
    private long userId;

    @Column(name = "title")
    private String title;

    @Column(name = "tstamp")
    private Timestamp timestamp;

    @Column(name = "link")
    private String link;

    @Column(name = "type")
    private ContentTypes type;


    private String frameworkName;
    private FrameworkCategories category;
    private String userName;
    private Integer userAuthVote;
    private boolean isVerify;
    private boolean isAdmin;
    private List<String> reportersNames = new ArrayList<>();

    public Content(){

    }

    public Content(long contentId, long frameworkId, long userId, String title, Timestamp timestamp, String link, ContentTypes type, String frameworkName, FrameworkCategories category, boolean isVerify, boolean isAdmin, String username,Integer userAuthVote) {
        this.contentId = contentId;
        this.frameworkId = frameworkId;
        this.userId = userId;
        this.title = title;
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
    public Content(long contentId, long frameworkId, long userId, String title, Timestamp timestamp, String link, ContentTypes type, String frameworkName, FrameworkCategories category, boolean isVerify, boolean isAdmin,String userName) {
        this.contentId = contentId;
        this.frameworkId = frameworkId;
        this.userId = userId;
        this.title = title;
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
