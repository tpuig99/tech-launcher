package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Comment;
import ar.edu.itba.paw.models.ReportComment;
import ar.edu.itba.paw.models.User;

import java.util.List;
import java.util.Optional;

public interface ReportCommentDao {
    Optional<ReportComment> getById(long reportId);
    List<ReportComment> getAll(long page, long pageSize);
    List<ReportComment> getByFramework(long frameworkId);
    Optional<ReportComment> getByComment(long commentId);
    void insert(Comment comment, User user, String description);
    void delete(long reportId);
    void deleteReportByComment(long commentId);

}
