package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Framework;
import ar.edu.itba.paw.models.FrameworkVote;
import ar.edu.itba.paw.models.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
public class FrameworkVoteHibernateDaoImpl implements FrameworkVoteDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<FrameworkVote> getAllByUser(long userId, long page, long pageSize) {
        final TypedQuery<FrameworkVote> query = em.createQuery("select fv from FrameworkVote fv where fv.user.id= :userId", FrameworkVote.class);
        query.setParameter("userId", userId);
        query.setFirstResult((int) ((page-1) * pageSize));
        query.setMaxResults((int) pageSize);
        return query.getResultList();
    }

    @Override
    public Optional<Integer> getAllCountByUser(long userId) {
        final TypedQuery<FrameworkVote> query = em.createQuery("select fv from FrameworkVote fv where fv.user.id= :userId", FrameworkVote.class);
        query.setParameter("userId", userId);
        return Optional.of(query.getResultList().size());
    }

    @Override
    public FrameworkVote insert(long frameworkId, long userId, int stars) {
        final FrameworkVote frameworkVote = new FrameworkVote( em.getReference(Framework.class, frameworkId), em.getReference(User.class, userId), stars);
        em.persist(frameworkVote);
        return frameworkVote;
    }

    @Override
    public void delete(long voteId) { em.remove(em.getReference(FrameworkVote.class,voteId)); }

    @Override
    public Optional<FrameworkVote> update(long voteId, int stars) {
        FrameworkVote frameworkVote = em.find(FrameworkVote.class, voteId);
        frameworkVote.setStars(stars);
        em.merge(frameworkVote);
        return Optional.of(frameworkVote);
    }
}
