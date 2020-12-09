package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.ReportComment;
import ar.edu.itba.paw.models.ReportContent;
import ar.edu.itba.paw.models.VerifyUser;

import java.util.List;

public class ModDTO {
    private List<VerifyUser> mods;
    private List<VerifyUser> verified;
    private List<VerifyUser> applicants;
    private List<ReportComment> reportedComments;
    private List<ReportContent> reportedContents;

    public List<VerifyUser> getMods() {
        return mods;
    }

    public void setMods(List<VerifyUser> mods) {
        this.mods = mods;
    }

    public List<VerifyUser> getVerified() {
        return verified;
    }

    public void setVerified(List<VerifyUser> verified) {
        this.verified = verified;
    }

    public List<VerifyUser> getApplicants() {
        return applicants;
    }

    public void setApplicants(List<VerifyUser> applicants) {
        this.applicants = applicants;
    }

    public List<ReportComment> getReportedComments() {
        return reportedComments;
    }

    public void setReportedComments(List<ReportComment> reportedComments) {
        this.reportedComments = reportedComments;
    }

    public List<ReportContent> getReportedContents() {
        return reportedContents;
    }

    public void setReportedContents(List<ReportContent> reportContents) {
        this.reportedContents = reportContents;
    }
}
