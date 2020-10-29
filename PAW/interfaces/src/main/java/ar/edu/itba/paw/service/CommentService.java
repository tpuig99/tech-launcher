package ar.edu.itba.paw.service;

import ar.edu.itba.paw.models.Comment;
import ar.edu.itba.paw.models.Framework;
import ar.edu.itba.paw.models.ReportComment;
import ar.edu.itba.paw.models.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CommentService {
    /*** Getters ***/
    Optional<Comment> getById(long contentId);
    List<Comment> getCommentsByFramework(long frameworkId,Long userId);
    List<Comment> getCommentsWithoutReferenceByFrameworkWithUser(long frameworkId,Long userId, long page);
    List<Comment> getCommentsByFrameworkAndUser(long frameworkId, long userId);
    List<Comment> getCommentsByUser(long userId, long page);

    Optional<Integer> getCommentsCountByUser(long userId);

    Map<Long, List<Comment>> getRepliesByFramework(long frameworkId);

    /*** Comment Methods ***/
    Comment insertComment(long frameworkId, long userId, String description, Long reference);
    void deleteComment(long commentId);
    Optional<Comment> changeComment(long commentId, String description);

    /***Votes methods***/
    Optional<Comment> vote(long commentId,long userId,int voteSign);


        /***Reports***/
    Optional<ReportComment> getReportById(long reportId);
    List<ReportComment> getAllReport(long page);
    List<ReportComment> getReportByFramework(long frameworkId);
    Optional<ReportComment> getReportByComment(long commentId);
    void addReport(long commentId,long userId,String description);
    void acceptReport(long commentId);
    void denyReport(long commentId);
    void deleteReport(long reportId);
}
