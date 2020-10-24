package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Comment;
import ar.edu.itba.paw.models.Framework;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.VerifyUser;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
public class VerifyUserHibernateDao implements VerifyUserDao {
    static private final boolean PENDING_DEFAULT =true;

    @PersistenceContext
    private EntityManager em;

    @Override
    public VerifyUser create(long userId, long frameworkId, long commentId) {
        final VerifyUser verifyUser = new VerifyUser(em.getReference(User.class,userId),em.getReference(Framework.class,frameworkId),em.getReference(Comment.class,commentId),PENDING_DEFAULT);
        em.persist(verifyUser);
        return verifyUser;
    }

    @Override
    public VerifyUser create(long userId, long frameworkId) {
        final VerifyUser verifyUser = new VerifyUser(em.getReference(User.class,userId),em.getReference(Framework.class,frameworkId),null,PENDING_DEFAULT);
        em.persist(verifyUser);
        return verifyUser;
    }

    @Override
    public List<VerifyUser> getByUser(long userId, boolean pending) {
        final TypedQuery<VerifyUser> query = em.createQuery("from VerifyUser as vu where vu.user.id = :userId and vu.pending = :pending", VerifyUser.class);
        query.setParameter("userId", userId);
        query.setParameter("pending", pending);
        return query.getResultList();
    }

    @Override
    public List<VerifyUser> getByFramework(long frameworkId, boolean pending) {
        final TypedQuery<VerifyUser> query = em.createQuery("from VerifyUser as vu where vu.framework.id = :frameworkId and vu.pending = :pending", VerifyUser.class);
        query.setParameter("frameworkId", frameworkId);
        query.setParameter("pending", pending);
        return query.getResultList();
    }

    @Override
    public List<VerifyUser> getByFrameworks(List<Long> frameworksIds, boolean pending, long page, long pageSize) {
        final TypedQuery<VerifyUser> query = em.createQuery("from VerifyUser as vu where vu.framework.id in :frameworksIds and vu.pending = :pending", VerifyUser.class);
        query.setParameter("frameworksIds", frameworksIds);
        query.setParameter("pending", pending);
        query.setFirstResult((int) ((page-1) * pageSize));
        query.setMaxResults((int) pageSize);
        return query.getResultList();
    }

    @Override
    public List<VerifyUser> getAllByUser(long userId) {
        final TypedQuery<VerifyUser> query = em.createQuery("from VerifyUser as vu where vu.user.id = :userId", VerifyUser.class);
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    @Override
    public List<VerifyUser> getAllByFramework(long frameworkId) {
        final TypedQuery<VerifyUser> query = em.createQuery("from VerifyUser as vu where vu.framework.id = :frameworkId", VerifyUser.class);
        query.setParameter("frameworkId", frameworkId);
        return query.getResultList();
    }

    @Override
    public Optional<VerifyUser> getByFrameworkAndUser(long frameworkId, long userId) {
        final TypedQuery<VerifyUser> query = em.createQuery("from VerifyUser as vu where vu.framework.id = :frameworkId and vu.user.id = :userId", VerifyUser.class);
        query.setParameter("frameworkId", frameworkId);
        query.setParameter("userId", userId);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public Optional<VerifyUser> getById(long verificationId) {
        return Optional.ofNullable(em.find(VerifyUser.class, verificationId));
    }

    @Override
    public List<VerifyUser> getByPending(boolean pending, long page, long pageSize) {
        if(!pending){
            final TypedQuery<VerifyUser> query = em.createQuery("from VerifyUser as vu where vu.pending = :pending", VerifyUser.class);
            query.setParameter("pending", pending);
            query.setFirstResult((int) ((page-1) * pageSize));
            query.setMaxResults((int) pageSize);
            return query.getResultList();
        }
        final TypedQuery<VerifyUser> query = em.createQuery("from VerifyUser as vu where vu.pending = :pending and vu.comment is not null", VerifyUser.class);
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
