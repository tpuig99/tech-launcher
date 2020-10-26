package ar.edu.itba.paw.models;

import jdk.jfr.ContentType;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportContent {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "content_report_content_id_seq")
    @SequenceGenerator(sequenceName = "content_report_content_id_seq", name = "content_report_content_id_seq", allocationSize = 1)
    @Column(name = "report_id")
    private Long reportId;

    @Column(name = "content_id", nullable = false)
    private long contentId;

    @Column(name = "framework_id", nullable = false)
    private long frameworkId;

    @Column(name = "user_id", nullable = false)
    private long userId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "tstamp", nullable = false)
    private Timestamp timestamp;

    @Column(name = "link", nullable = false)
    private String link;

    @Column(name = "type", nullable = false)
    private ContentTypes type;

    private String frameworkName;
    private FrameworkCategories category;
    private String userNameOwner;
    private String reportDescription;
    private Map<Long,String> userNameReporters;

    public ReportContent(){

    }

    public ReportContent( long contentId, long userId, String reportDescription){
        this.contentId = contentId;
        this.userId = userId;
        this.reportDescription = reportDescription;
    }

    public ReportContent(long contentId, long frameworkId, long userId, String title, Timestamp timestamp, String link, ContentTypes type, String frameworkName, FrameworkCategories category, String userNameOwner, String reportDescription) {
        this.contentId = contentId;
        this.frameworkId = frameworkId;
        this.userId = userId;
        this.title = title;
        this.timestamp = timestamp;
        this.link = link;
        this.type = type;
        this.frameworkName = frameworkName;
        this.category = category;
        this.userNameOwner = userNameOwner;
        this.reportDescription = reportDescription;
        userNameReporters = new HashMap<>();
    }

    public ReportContent(long contentId, long frameworkId, long userId, String title, String reportDescription, Timestamp timestamp, String link, ContentTypes type, String frameworkName, String userNameOwner, FrameworkCategories category) {
        this.contentId = contentId;
        this.frameworkId = frameworkId;
        this.userId = userId;
        this.title = title;
        this.reportDescription = reportDescription;
        this.timestamp = timestamp;
        this.link = link;
        this.type = type;
        this.frameworkName = frameworkName;
        this.userNameOwner = userNameOwner;
        this.userNameReporters = new HashMap<>();
        this.category = category;
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

    public String getReportDescription() {
        return reportDescription;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }


    public String getFrameworkName() {
        return frameworkName;
    }

    public String getUserNameOwner() {
        return userNameOwner;
    }

    public Map<Long,String> getUserNameReporters() {
        return userNameReporters;
    }

    public FrameworkCategories getCategory() {
        return category;
    }

    public String getCategoryAsString(){return category.getNameCat();}

    public String getLink() {
        return link;
    }

    public ContentTypes getType() {
        return type;
    }
    public String getTypeAsString(){return type.name();}

    public List<Long> getReportsIds(){
        List<Long> list = new ArrayList<>();
        list.addAll(userNameReporters.keySet());
        return list;
    }
    public List<String> getReportsUserName(){
        List<String> list = new ArrayList<>();
        list.addAll(userNameReporters.values());
        return list;
    }
    public void addUserReporter(long reportId,String userName){
        userNameReporters.put(reportId,userName);
    }
}
