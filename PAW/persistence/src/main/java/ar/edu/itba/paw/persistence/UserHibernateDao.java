package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
public class UserHibernateDao implements UserDao {
    static private String DESC_DEFAULT ="";
    static private Boolean ENABLED_DEFAULT =false;
    static private byte[] PICTURE_DEFAULT;
    static private Boolean ALLOW_DEFAULT =true;

    @PersistenceContext
    private EntityManager em;

    @Override
    public User create(String username, String mail, String password) {
        final User user = new User(username,mail,password,ENABLED_DEFAULT,DESC_DEFAULT,ALLOW_DEFAULT,PICTURE_DEFAULT);
        em.persist(user);
        return user;
    }

    @Override
    public int delete(long userId) {
        return 0;
    }

    @Override
    public Optional<User> update(long userId, String username, String mail, String password) {
        return Optional.empty();
    }

    @Override
    public List<String> getMails() {
        return null;
    }

    @Override
    public List<String> getUserNames() {
        return null;
    }

    @Override
    public void setEnable(long id) {

    }

    @Override
    public void updateDescription(long userId, String description) {

    }

    @Override
    public void updatePicture(long id, byte[] picture) {

    }

    @Override
    public void updatePassword(long userId, String password) {

    }

    @Override
    public void updateModAllow(long userId, boolean allow) {

    }

    @Override
    public Optional<User> findByUsername(final String username) {
        final TypedQuery<User> query = em.createQuery("from User as u where u.username = :username", User.class);
        query.setParameter("username", username);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public Optional<User> findByUsernameOrMail(String username, String mail) {
        final TypedQuery<User> query = em.createQuery("from User as u where u.username = :username or u.mail = :mail", User.class);
        query.setParameter("username", username);
        query.setParameter("mail", mail);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public Optional<User> findByMail(String mail) {
        final TypedQuery<User> query = em.createQuery("from User as u where u.mail = :mail", User.class);
        query.setParameter("mail", mail);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public List<User> getAll() {
        final TypedQuery<User> query = em.createQuery("from User as u", User.class);
        return query.getResultList();
    }

    @Override
    public Optional<User> findById(long id) {
        return Optional.ofNullable(em.find(User.class, id));
    }
}

