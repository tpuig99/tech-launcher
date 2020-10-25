package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class ReportCommentHiberateDaoImpl implements ReportCommentDao{
    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<ReportComment> getById(long reportId) {
        return Optional.empty();
    }

    @Override
    public List<ReportComment> getAll(long page, long pageSize) {
        final TypedQuery<ReportComment> query = em.createQuery("from CommentVote", ReportComment.class);
        return query.getResultList();
    }

    @Override
    public List<ReportComment> getByFramework(long frameworkId) {

        final TypedQuery<ReportComment> query = em.createQuery("from ReportComment rc where rc.frameworkId = :frameworkId", ReportComment.class);
        query.setParameter("frameworkId", frameworkId);
        return query.getResultList();
    }

    @Override
    public Optional<ReportComment> getByComment(long commentId) {
        final TypedQuery<ReportComment> query = em.createQuery("from ReportComment rc where rc.comment.id = :commentId", ReportComment.class);
        query.setParameter("commentId", commentId);
        return query.getResultList().stream().findFirst();
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
        final TypedQuery<ReportComment> query = em.createQuery("delete from ReportComment rc where rc.comment.id = :commentId", ReportComment.class);
        query.setParameter("commentId", commentId).executeUpdate();

    }
}
