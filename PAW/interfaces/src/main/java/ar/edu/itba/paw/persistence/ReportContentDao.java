package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.ReportContent;

import java.util.List;
import java.util.Optional;

public interface ReportContentDao {
    Optional<ReportContent> getById(long reportId);
    List<ReportContent> getAll();
    List<ReportContent> getByFramework(long frameworkId);
    Optional<ReportContent> getByContent(long contentId);
    void add(long contentId,long userId,String description);
    void delete(long reportId);
    void deleteByContent(long contentId);

}
