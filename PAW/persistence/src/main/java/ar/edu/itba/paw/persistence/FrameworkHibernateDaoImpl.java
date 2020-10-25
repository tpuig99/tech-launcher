package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.*;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public class FrameworkHibernateDaoImpl implements FrameworkDao {

    @PersistenceContext
    private EntityManager em;


    @Override
    public Optional<Framework> findById(long id) {
        return Optional.ofNullable(em.find(Framework.class, id));
    }

    @Override
    public List<String> getFrameworkNames() {
        return null;
    }

    /*TODO: Add pagination*/
    @Override
    public List<Framework> getByCategory(FrameworkCategories category, long page, long pageSize) {
        final TypedQuery<Framework> query = em.createQuery("from Framework f where f.category= :category", Framework.class);
        query.setParameter("category", category);
        return query.getResultList();
    }

    @Override
    public List<Framework> getByType(FrameworkType type) {
        final TypedQuery<Framework> query = em.createQuery("from Framework f where f.type= :frameworkType", Framework.class);
        query.setParameter("frameworkType", type);
        return query.getResultList();
    }

    @Override
    public List<Framework> getByCategoryAndType(FrameworkType type, FrameworkCategories category) {
        final TypedQuery<Framework> query = em.createQuery("from Framework f where f.category= :category and f.type= :frameworkType", Framework.class);
        query.setParameter("category", category);
        query.setParameter("frameworkType", type);
        return query.getResultList();
    }

    @Override
    public List<Framework> getByCategoryAndWord(FrameworkCategories category, String word) {
        return null;
    }

    @Override
    public List<Framework> getByTypeAndWord(FrameworkType type, String word) {
        return null;
    }

    @Override
    public List<Framework> getByCategoryAndTypeAndWord(FrameworkType type, FrameworkCategories category, String word) {
        return null;
    }

    @Override
    public List<Framework> getAll() {
        return null;
    }

    @Override
    public List<Framework> getByWord(String toSearch) {
        return null;
    }

    @Override
    public List<Framework> getUserInterests(long userId) {
        return null;
    }

    @Override
    public List<Framework> getByCategoryOrType(FrameworkType frameType, FrameworkCategories frameCategory) {
        return null;
    }

    @Override
    public List<Framework> getByMultipleCategories(List<FrameworkCategories> categories) {
        return null;
    }

    @Override
    public List<Framework> getByMinStars(int stars) {
        return null;
    }

    @Override
    public List<Framework> getByMultipleTypes(List<FrameworkType> types) {
        return null;
    }

    @Override
    public List<Framework> getByUser(long userId, long page, long pageSize) {
        return null;
    }

    @Override
    public Optional<Integer> getByUserCount(long userId) {
        return Optional.empty();
    }

    @Override
    public List<Framework> search(String toSearch, List<FrameworkCategories> categories, List<FrameworkType> types, Integer starsLeft, Integer starsRight, boolean nameFlag, Integer commentAmount, Timestamp lastComment, Timestamp lastUpdated, Integer order, long page, long pageSize) {
        return null;
    }

    @Override
    public Optional<Framework> create(String name, FrameworkCategories category, String description, String introduction, FrameworkType type, long userId, byte[] picture) {
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        final Framework framework = new Framework();

        framework.setName(name);
        framework.setCategory(category);
        framework.setDescription(description);
        framework.setIntroduction(introduction);
        framework.setType(type);
        framework.setAuthor(em.getReference(User.class, userId));
        framework.setPublishDate(ts);
        framework.setPicture(picture);

        em.persist(framework);
        return Optional.of(framework);
    }

    @Override
    public Optional<Framework> update(long id, String name, FrameworkCategories category, String description, String introduction, FrameworkType type, byte[] picture) {
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        Framework framework = em.find(Framework.class, id);

        framework.setName(name);
        framework.setCategory(category);
        framework.setDescription(description);
        framework.setIntroduction(introduction);
        framework.setType(type);
        framework.setPublishDate(ts);
        framework.setPicture(picture);

        em.merge(framework);
        return Optional.of(framework);
    }

    @Override
    public void delete(long id) {
        em.remove(em.getReference(Framework.class, id));
    }

    @Override
    public Integer searchResultsNumber(String toSearch, List<FrameworkCategories> categories, List<FrameworkType> types, Integer starsLeft, Integer starsRight, boolean nameFlag, Integer commentAmount, Timestamp lastComment, Timestamp lastUpdated) {
        return null;
    }
}
