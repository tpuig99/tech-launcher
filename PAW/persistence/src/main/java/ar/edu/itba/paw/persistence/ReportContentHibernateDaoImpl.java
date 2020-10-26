package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.ReportContent;
import ar.edu.itba.paw.models.VerificationToken;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

@Repository
public class ReportContentHibernateDaoImpl implements ReportContentDao {
    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<ReportContent> getById(long reportId) {
        return Optional.ofNullable(em.find(ReportContent.class, reportId));
    }

    @Override
    public List<ReportContent> getAll(long page, long pageSize) {
        final TypedQuery<ReportContent> query = em.createQuery("FROM ReportContent", ReportContent.class);
        query.setMaxResults((int) pageSize);
        query.setFirstResult((int) ((page-1)*pageSize));
        return query.getResultList();
    }

    @Override
    public List<ReportContent> getByFramework(long frameworkId) {
        final TypedQuery<ReportContent> query = em.createQuery("FROM ReportContent rc WHERE rc.frameworkId = :frameworkId", ReportContent.class);
        query.setParameter("frameworkId", frameworkId);
        return query.getResultList();
    }

    @Override
    public List<ReportContent> getByFrameworks(List<Long> frameworksIds, long page, long pageSize) {
        return null;
    }

    @Override
    public Optional<ReportContent> getByContent(long contentId) {
        final TypedQuery<ReportContent> query = em.createQuery("FROM ReportContent rc WHERE rc.contentId = :contentId", ReportContent.class);
        query.setParameter("contentId",contentId);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public void add(long contentId, long userId, String description) {
        final ReportContent rc = new ReportContent(contentId, userId, description);
        em.persist(rc);
    }

    @Override
    public void delete(long reportId) {
        em.remove(em.getReference(ReportContent.class, reportId));
    }

    @Override
    public void deleteByContent(long contentId) {
        em.createQuery("DELETE FROM ReportContent rc WHERE rc.contentId = :contentId")
                .setParameter("contentId", contentId)
                .executeUpdate();
    }
}
