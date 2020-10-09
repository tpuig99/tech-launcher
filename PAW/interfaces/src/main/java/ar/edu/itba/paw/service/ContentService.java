package ar.edu.itba.paw.service;

import ar.edu.itba.paw.models.Content;
import ar.edu.itba.paw.models.Content;
import ar.edu.itba.paw.models.ContentTypes;
import ar.edu.itba.paw.models.ReportContent;

import java.net.URL;
import java.util.List;
import java.util.Optional;

public interface ContentService {
    /*** Getters ***/
    Optional<Content> getById(long contentId);
    List<Content> getContentByFramework(long frameworkId);
    List<Content> getContentByFrameworkAndUser(long frameworkId, long userId);
    List<Content> getContentByUser(long userId, long page);
    List<Content> getContentByFrameworkAndType(long frameworkId, ContentTypes type, long page);
    List<Content> getContentByFrameworkAndTypeAndTitle(long frameworkId, ContentTypes type,String title);
    /***Content methods***/
    Content insertContent(long frameworkId, long userId, String title, String link, ContentTypes type);
    int deleteContent(long contentId);
    Optional<Content> changeContent(long contentId, String title, String link, ContentTypes types);

    /*** Votes methods ***/
    void voteUp(long contentId, long userId);
    void voteDown(long contentId,long userId);

    /*** Reports ***/
    Optional<ReportContent> getReporstById(long reportId);
    List<ReportContent> getAllReports();
    List<ReportContent> getReportsByFramework(long frameworkId);
    List<ReportContent> getReportsByFrameworks( List<Long> frameworksIds);
    Optional<ReportContent> getReportsByContent(long contentId);
    void addReport(long contentId,long userId,String description);
    void acceptReport(long contentId);
    void denyReport(long contentId);
    void deleteReport(long reportId);
}
