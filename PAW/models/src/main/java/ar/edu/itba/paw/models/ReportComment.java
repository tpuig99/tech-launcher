package ar.edu.itba.paw.models;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.*;

@Entity
@Table(name = "comment_report")
public class ReportComment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "comment_report_report_id_seq")
    @SequenceGenerator(sequenceName = "comment_report_report_id_seq", name = "comment_report_report_id_seq", allocationSize = 1)
    @Column(name = "report_id")
    private Long reportId;

    @Column(name="description", nullable = false)
    private String reportDescription;

    //TODO user and comment should be UNIQUE(user_id,conmment_id), dont know how to do it
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", nullable = false)
    private Comment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    private long frameworkId;
    private String commentDescription;
    private Timestamp timestamp;
    private Long reference;
    private String frameworkName;
    private String userNameOwner;
    private Map<Long,String> userNameReporters;
    private FrameworkCategories category;

    public ReportComment(Comment comment, long frameworkId, User user, String commentDescription,String reportDescription, Timestamp timestamp, Long reference, String frameworkName, String userNameOwner,FrameworkCategories category) {
        this.comment = comment;
        this.frameworkId = frameworkId;
        this.user = user;
        this.commentDescription = commentDescription;
        this.reportDescription = reportDescription;
        this.timestamp = timestamp;
        this.reference = reference;
        this.frameworkName = frameworkName;
        this.userNameOwner = userNameOwner;
        this.userNameReporters = new HashMap<>();
        this.category = category;
    }

    public ReportComment() {

    }

    public long getCommentId() {
        return comment.getCommentId();
    }

    public long getFrameworkId() {
        return frameworkId;
    }

    public long getUserId() {
        return user.getId();
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

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setReportDescription(String reportDescription) {
        this.reportDescription = reportDescription;
    }
}
