package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.ReportComment;
import ar.edu.itba.paw.models.ReportContent;

import javax.ws.rs.core.UriInfo;

public class ReportDTO {
    private String reportDescription;
    private String commentDescription;
    private String contentLink;
    private String frameworkName;
    private String owner;
    private String reported;
    private String location;

    public static ReportDTO fromReportComment (ReportComment reportComment, UriInfo uriInfo) {
        ReportDTO dto = new ReportDTO();
        dto.reportDescription = reportComment.getReportDescription();
        dto.commentDescription = reportComment.getCommentDescription();
        dto.frameworkName = reportComment.getFrameworkName();
        dto.owner = reportComment.getUserNameOwner();
        dto.reported = reportComment.getUserReporterName();
        dto.location = "mod/reports/comment/" + reportComment.getCommentId();
        return dto;
    }

    public static ReportDTO fromReportContent (ReportContent reportContent, UriInfo uriInfo) {
        ReportDTO dto = new ReportDTO();
        dto.reportDescription = reportContent.getReportDescription();
        dto.contentLink = reportContent.getLink();
        dto.frameworkName = reportContent.getFrameworkName();
        dto.owner = reportContent.getUserNameOwner();
        dto.reported = reportContent.getUserReporterName();
        dto.location = "mod/reports/content/" + reportContent.getContentId();
        return dto;
    }


    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getReportDescription() {
        return reportDescription;
    }

    public void setReportDescription(String reportDescription) {
        this.reportDescription = reportDescription;
    }

    public String getCommentDescription() {
        return commentDescription;
    }

    public void setCommentDescription(String commentDescription) {
        this.commentDescription = commentDescription;
    }

    public String getContentLink() {
        return contentLink;
    }

    public void setContentLink(String contentLink) {
        this.contentLink = contentLink;
    }

    public String getFrameworkName() {
        return frameworkName;
    }

    public void setFrameworkName(String frameworkName) {
        this.frameworkName = frameworkName;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getReported() {
        return reported;
    }

    public void setReported(String reported) {
        this.reported = reported;
    }
}
