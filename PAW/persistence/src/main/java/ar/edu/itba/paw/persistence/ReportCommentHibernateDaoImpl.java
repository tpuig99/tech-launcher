package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Comment;
import ar.edu.itba.paw.models.ReportComment;
import ar.edu.itba.paw.models.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class ReportCommentHibernateDaoImpl implements ReportCommentDao{
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<ReportComment> getAll(long page, long pageSize) {
        Query pagingQuery = em.createNativeQuery("SELECT report_id FROM comment_report " + " LIMIT " + String.valueOf(pageSize) + " OFFSET " + String.valueOf((page-1)*pageSize));
        @SuppressWarnings("unchecked")
        List<Long> resultList = ((List<Number>)pagingQuery.getResultList()).stream().map(Number::longValue).collect(Collectors.toList());

        if(!resultList.isEmpty()) {
            TypedQuery<ReportComment> query = em.createQuery("from ReportComment as c where c.reportId in (:resultList)", ReportComment.class);
            query.setParameter("resultList", resultList);
            return query.getResultList();
        }else{
            return new ArrayList<>();
        }
    }

    @Override
    public Optional<Integer> getAllReportsAmount() {
        TypedQuery<ReportComment> query = em.createQuery("from ReportComment ", ReportComment.class);
        return Optional.of(query.getResultList().size());
    }

    @Override
    public void insert(long commentId, long userId, String description) {
        ReportComment reportComment = new ReportComment();
        reportComment.setComment(em.getReference(Comment.class, commentId));
        reportComment.setUser(em.getReference(User.class, userId));
        reportComment.setReportDescription(description);
        em.persist(reportComment);
    }

    @Override
    public void delete(long reportId) {
        em.remove(em.getReference(ReportComment.class,reportId));
    }

    @Override
    public void deleteReportByComment(long commentId) {
        final TypedQuery<Long> query = em.createQuery("select rc.reportId from ReportComment rc where rc.comment.commentId = :commentId", Long.class);
        query.setParameter("commentId", commentId);
        delete(query.getSingleResult());
    }
}
