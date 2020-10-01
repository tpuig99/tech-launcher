package ar.edu.itba.paw.service;

import ar.edu.itba.paw.models.Comment;
import ar.edu.itba.paw.models.ReportComment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CommentService {
    Optional<Comment> getById(long contentId);
    List<Comment> getCommentsByFramework(long frameworkId,Long userId);
    List<Comment> getCommentsWithoutReferenceByFramework(long frameworkId);
    List<Comment> getCommentsByFrameworkAndUser(long frameworkId, long userId);
    List<Comment> getCommentsByUser(long userId);
    Map<Long, List<Comment>> getRepliesByFramework(long frameworkId);
    Comment insertComment(long frameworkId, long userId, String description, Long reference);
    int deleteComment(long commentId);
    Optional<Comment> changeComment(long commentId, String description);
    Optional<Comment> voteUp(long commentId,long userId);
    Optional<Comment> voteDown(long commentId,long userId);

    /****comment report*****/
    Optional<ReportComment> getReportById(long reportId);
    List<ReportComment> getAllReport();
    List<ReportComment> getReportByFramework(long frameworkId);
    Optional<ReportComment> getReportByComment(long commentId);
    void addReport(long commentId,long userId,String description);
    void deleteReport(long reportId);
    void deleteReportByComment(long commentId);
}
