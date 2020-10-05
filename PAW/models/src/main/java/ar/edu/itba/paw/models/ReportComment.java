package ar.edu.itba.paw.models;

import java.sql.Timestamp;
import java.util.*;

public class ReportComment {
    private long commentId;
    private long frameworkId;
    private long userId;
    private String commentDescription;
    private String reportDescription;
    private Timestamp timestamp;
    private Long reference;
    private String frameworkName;
    private String userNameOwner;
    private Map<Long,String> userNameReporters;
    private FrameworkCategories category;

    public ReportComment(long commentId, long frameworkId, long userId, String commentDescription,String reportDescription, Timestamp timestamp, Long reference, String frameworkName, String userNameOwner,FrameworkCategories category) {
        this.commentId = commentId;
        this.frameworkId = frameworkId;
        this.userId = userId;
        this.commentDescription = commentDescription;
        this.reportDescription = reportDescription;
        this.timestamp = timestamp;
        this.reference = reference;
        this.frameworkName = frameworkName;
        this.userNameOwner = userNameOwner;
        this.userNameReporters = new HashMap<>();
        this.category = category;
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

    public String getCommentDescription() {
        return commentDescription;
    }

    public String getReportDescription() {
        return reportDescription;
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
