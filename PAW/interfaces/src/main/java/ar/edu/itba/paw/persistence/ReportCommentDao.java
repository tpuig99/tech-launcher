package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.ReportComment;

import java.util.List;
import java.util.Optional;

public interface ReportCommentDao {
    Optional<ReportComment> getById(long reportId);
    List<ReportComment> getAll(long page, long pageSize);
    List<ReportComment> getByFramework(long frameworkId);
    Optional<ReportComment> getByComment(long commentId);
    void add(long commentId,long userId,String description);
    void delete(long reportId);
    void deleteReportByComment(long commentId);

}
