package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.VerificationToken;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Optional;

@Repository
public class VerificationTokenHibernateDaoImpl implements VerificationTokenDao {
    @PersistenceContext
    private EntityManager em;

    @Override
    public void insert(long userId, String token) {
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(ts);
        calendar.add(Calendar.MINUTE,60*24);
        ts = new Timestamp(calendar.getTime().getTime());
        final VerificationToken vt = new VerificationToken(token, userId, ts);
        em.persist(vt);

    }

    @Override
    public Optional<VerificationToken> getById(long tokenId) {
        return Optional.ofNullable(em.find(VerificationToken.class, tokenId));
    }

    @Override
    public Optional<VerificationToken> getByUser(long userId) {
        final TypedQuery<VerificationToken> query = em.createQuery("FROM VerificationToken vt WHERE vt.userId = :userId", VerificationToken.class);
        query.setParameter("userId", userId);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public void change(long tokenId, String token) {
        VerificationToken vt = em.find(VerificationToken.class, tokenId);
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(ts);
        calendar.add(Calendar.MINUTE,60*24);
        ts = new Timestamp(calendar.getTime().getTime());
        em.merge(vt);
    }

    @Override
    public Optional<VerificationToken> getByToken(String token) {
        final TypedQuery<VerificationToken> query = em.createQuery("FROM VerificationToken vt WHERE vt.token = :token", VerificationToken.class);
        query.setParameter("token", token);
        return query.getResultList().stream().findFirst();
    }
}
