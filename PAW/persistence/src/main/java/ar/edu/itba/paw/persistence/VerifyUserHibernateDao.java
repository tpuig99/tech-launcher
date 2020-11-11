package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Comment;
import ar.edu.itba.paw.models.Framework;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.VerifyUser;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
public class VerifyUserHibernateDao implements VerifyUserDao {
    static private final boolean PENDING_DEFAULT =true;

    @PersistenceContext
    private EntityManager em;

    @Override
    public VerifyUser create(User user, Framework framework, Comment comment) {
        final VerifyUser verifyUser = new VerifyUser(user,framework,comment,PENDING_DEFAULT);
        em.persist(verifyUser);
        return verifyUser;
    }


    @Override
    public List<VerifyUser> getVerifyForCommentByFrameworks(List<Long> frameworksIds, boolean pending, long page, long pageSize) {
        final TypedQuery<VerifyUser> query = em.createQuery("from VerifyUser as vu where vu.framework.id in :frameworksIds and vu.pending = :pending", VerifyUser.class);
        query.setParameter("frameworksIds", frameworksIds);
        query.setParameter("pending", pending);
        query.setFirstResult((int) ((page-1) * pageSize));
        query.setMaxResults((int) pageSize);
        return query.getResultList();
    }

    @Override
    public Optional<VerifyUser> getById(long verificationId) {
        return Optional.ofNullable(em.find(VerifyUser.class, verificationId));
    }

    @Override
    public List<VerifyUser> getVerifyForCommentByPending(boolean pending, long page, long pageSize) {
        final TypedQuery<VerifyUser> query;
        if(!pending)
            query = em.createQuery("from VerifyUser as vu where vu.pending = :pending", VerifyUser.class);
        else
            query = em.createQuery("from VerifyUser as vu where vu.pending = :pending and vu.comment is not null", VerifyUser.class);
        query.setParameter("pending", pending);
        query.setFirstResult((int) ((page-1) * pageSize));
        query.setMaxResults((int) pageSize);
        return query.getResultList();
    }

    @Override
    public List<VerifyUser> getApplicantsByPending(boolean pending, long page, long pageSize) {
        final TypedQuery<VerifyUser> query = em.createQuery("from VerifyUser as vu where vu.pending = :pending and vu.comment is null", VerifyUser.class);
        query.setParameter("pending", pending);
        query.setFirstResult((int) ((page-1) * pageSize));
        query.setMaxResults((int) pageSize);
        return query.getResultList();
    }

    @Override
    public List<VerifyUser> getApplicantsByFrameworks(List<Long> frameworksIds, long page, long pageSize) {
        final TypedQuery<VerifyUser> query = em.createQuery("from VerifyUser as vu where vu.framework.id in :frameworksIds and vu.comment is null", VerifyUser.class);
        query.setParameter("frameworksIds", frameworksIds);
        query.setFirstResult((int) ((page-1) * pageSize));
        query.setMaxResults((int) pageSize);
        return query.getResultList();
    }

    @Override
    public Optional<Integer> getVerifyForCommentByPendingAmount(boolean pending) {
        final TypedQuery<VerifyUser> query;
        if(!pending)
            query = em.createQuery("from VerifyUser as vu where vu.pending = :pending", VerifyUser.class);
        else
            query = em.createQuery("from VerifyUser as vu where vu.pending = :pending and vu.comment is not null", VerifyUser.class);
        query.setParameter("pending", pending);
        return Optional.of(query.getResultList().size());
    }

    @Override
    public Optional<Integer> getVerifyForCommentByFrameworkAmount(List<Long> frameworksIds, boolean pending) {
        final TypedQuery<VerifyUser> query = em.createQuery("from VerifyUser as vu where vu.framework.id in :frameworksIds and vu.pending = :pending", VerifyUser.class);
        query.setParameter("frameworksIds", frameworksIds);
        query.setParameter("pending", pending);
        return Optional.of(query.getResultList().size());
    }

    @Override
    public Optional<Integer> getApplicantsByPendingAmount(boolean pending) {
        final TypedQuery<VerifyUser> query = em.createQuery("from VerifyUser as vu where vu.pending = :pending and vu.comment is null", VerifyUser.class);
        query.setParameter("pending", pending);
        return Optional.of(query.getResultList().size());
    }

    @Override
    public Optional<Integer> getApplicantsByFrameworkAmount(List<Long> frameworksIds, boolean pending) {
        final TypedQuery<VerifyUser> query = em.createQuery("from VerifyUser as vu where vu.framework.id in :frameworksIds and vu.comment is null", VerifyUser.class);
        query.setParameter("frameworksIds", frameworksIds);
        return Optional.of(query.getResultList().size());
    }

    @Override
    public void delete(long verificationId) {
        em.remove(em.getReference(VerifyUser.class,verificationId));
    }

    @Override
    public void deleteVerificationByUser(long userId) {
        em.createQuery("DELETE FROM VerifyUser vu WHERE vu.user.id = :userId ")
                .setParameter("userId", userId)
                .executeUpdate();
    }

    @Override
    public void verify(long verificationId) {
        VerifyUser verifyUser = em.find(VerifyUser.class,verificationId);
        verifyUser.setPending(false);
        em.merge(verifyUser);
    }
}
