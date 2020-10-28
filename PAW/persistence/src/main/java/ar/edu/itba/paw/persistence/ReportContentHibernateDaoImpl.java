package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.*;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        Query pagingQuery = em.createNativeQuery("SELECT report_id FROM content_report " + " LIMIT " + String.valueOf(pageSize) + " OFFSET " + String.valueOf((page-1)*pageSize));
        @SuppressWarnings("unchecked")
        List<Long> resultList = ((List<Number>)pagingQuery.getResultList()).stream().map(Number::longValue).collect(Collectors.toList());

        if(!resultList.isEmpty()) {
            TypedQuery<ReportContent> query = em.createQuery("from ReportContent as c where c.reportId in (:resultList)", ReportContent.class);
            query.setParameter("resultList", resultList);
            return query.getResultList();
        }else{
            return new ArrayList<>();
        }
    }

    @Override
    public List<ReportContent> getByFramework(long frameworkId) {
        final TypedQuery<ReportContent> query = em.createQuery("FROM ReportContent rc WHERE rc.content.framework.id = :frameworkId", ReportContent.class);
        query.setParameter("frameworkId", frameworkId);
        return query.getResultList();
    }

    @Override
    public List<ReportContent> getByFrameworks(List<Long> frameworksIds, long page, long pageSize) {
        final TypedQuery<ReportContent> query = em.createQuery("FROM ReportContent rc where rc.content.framework.id in :frameworksIds", ReportContent.class);
        query.setParameter("frameworksIds", frameworksIds);
        query.setFirstResult((int) ((page-1) * pageSize));
        query.setMaxResults((int) pageSize);
        return query.getResultList();
    }

    @Override
    public Optional<ReportContent> getByContent(long contentId) {
        final TypedQuery<ReportContent> query = em.createQuery("FROM ReportContent rc WHERE rc.content.id = :contentId", ReportContent.class);
        query.setParameter("contentId",contentId);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public void add(long contentId, long userId, String description) {
        final ReportContent rc = new ReportContent(em.getReference(Content.class,contentId), em.getReference(User.class,userId), description);
        em.persist(rc);
    }

    @Override
    public void delete(long reportId) {
        em.remove(em.getReference(ReportContent.class, reportId));
    }

    @Override
    public void deleteByContent(long contentId) {
        em.createQuery("DELETE FROM ReportContent rc WHERE rc.content.id = :contentId")
                .setParameter("contentId", contentId)
                .executeUpdate();
    }
}
