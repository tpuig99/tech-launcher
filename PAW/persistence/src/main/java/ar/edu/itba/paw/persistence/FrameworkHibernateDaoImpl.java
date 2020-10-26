package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Framework;
import ar.edu.itba.paw.models.FrameworkCategories;
import ar.edu.itba.paw.models.FrameworkType;
import ar.edu.itba.paw.models.User;
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
        final TypedQuery<String> query = em.createQuery("select f.name from Framework f", String.class);
        return query.getResultList();
    }

    
    @Override
    public List<Framework> getByCategory(FrameworkCategories category, long page, long pageSize) {
        final TypedQuery<Framework> query = em.createQuery("select f from Framework f where f.category= :category", Framework.class);
        query.setParameter("category", category);
        query.setFirstResult((int) ((page-1) * pageSize));
        query.setMaxResults((int) pageSize);
        return query.getResultList();
    }

    @Override
    public List<Framework> getByType(FrameworkType type) {
        final TypedQuery<Framework> query = em.createQuery("select f from Framework f where f.type= :frameworkType", Framework.class);
        query.setParameter("frameworkType", type);
        return query.getResultList();
    }

    @Override
    public List<Framework> getByCategoryAndType(FrameworkType type, FrameworkCategories category) {
        final TypedQuery<Framework> query = em.createQuery("select f from Framework f where f.category= :category and f.type= :frameworkType", Framework.class);
        query.setParameter("category", category);
        query.setParameter("frameworkType", type);
        return query.getResultList();
    }

    @Override
    public List<Framework> getByCategoryAndWord(FrameworkCategories category, String word) {
        final TypedQuery<Framework> query = em.createQuery("select f from Framework f where lower(f.name) like :toSearch or f.category = :category", Framework.class);
        query.setParameter("toSearch", "%"+word.toLowerCase()+"%");
        query.setParameter("category", category);
        return query.getResultList();
    }

    @Override
    public List<Framework> getByTypeAndWord(FrameworkType type, String word) {
        final TypedQuery<Framework> query = em.createQuery("select f from Framework f where lower(f.name) like :toSearch or f.type = :type", Framework.class);
        query.setParameter("toSearch", "%"+word.toLowerCase()+"%");
        query.setParameter("type", type);
        return query.getResultList();
    }

    @Override
    public List<Framework> getByCategoryAndTypeAndWord(FrameworkType type, FrameworkCategories category, String word) {
        final TypedQuery<Framework> query = em.createQuery("select f from Framework f where lower(f.name) like :toSearch or f.type = :type or f.category = :category", Framework.class);
        query.setParameter("toSearch", "%"+word.toLowerCase()+"%");
        query.setParameter("type", type);
        query.setParameter("category", category);
        return query.getResultList();
    }

    @Override
    public List<Framework> getAll() {
        final TypedQuery<Framework> query = em.createQuery("select f from Framework f", Framework.class);
        return query.getResultList();
    }

    @Override
    public List<Framework> getByWord(String word) {
        final TypedQuery<Framework> query = em.createQuery("select f from Framework f where lower(f.name) like :toSearch", Framework.class);
        query.setParameter("toSearch", "%"+word.toLowerCase()+"%");
        return query.getResultList();
    }

    /* TODO: when Content is ready, update this query */
    @Override
    public List<Framework> getUserInterests(long userId) {
        final TypedQuery<Framework> query = em.createQuery("select f from Framework f inner join Content c on f.id = c.framework_id where c.user.id = :userId", Framework.class);
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    @Override
    public List<Framework> getByCategoryOrType(FrameworkType frameType, FrameworkCategories frameCategory) {
        final TypedQuery<Framework> query = em.createQuery("select f from Framework f where f.category= :category or f.type= :frameworkType", Framework.class);
        query.setParameter("category", frameCategory);
        query.setParameter("frameworkType", frameType);
        return query.getResultList();
    }

    @Override
    public List<Framework> getByMultipleCategories(List<FrameworkCategories> categories) {
        final TypedQuery<Framework> query = em.createQuery("select f from Framework f where f.category in (:categories)", Framework.class);
        query.setParameter("categories", categories);
        return query.getResultList();
    }

    @Override
    public List<Framework> getByMinStars(int stars) {
        final TypedQuery<Framework> query = em.createQuery("select f from Framework f inner join f.frameworkVotes v group by f having coalesce(avg(v.stars), 0) >= :stars", Framework.class);
        query.setParameter("stars", stars);
        return query.getResultList();
    }

    @Override
    public List<Framework> getByMultipleTypes(List<FrameworkType> types) {
        final TypedQuery<Framework> query = em.createQuery("select f from Framework f where f.type in (:types)", Framework.class);
        query.setParameter("types", types);
        return query.getResultList();
    }

    /* TODO: add pagination  */
    @Override
    public List<Framework> getByUser(long userId, long page, long pageSize) {
        final TypedQuery<Framework> query = em.createQuery("select f from Framework f where f.author.id = :userId", Framework.class);
        query.setParameter("userId", userId);
        query.setFirstResult((int) ((page-1) * pageSize));
        query.setMaxResults((int) pageSize);
        return query.getResultList();
    }

    @Override
    public Optional<Integer> getByUserCount(long userId) {
        final TypedQuery<Framework> query = em.createQuery("select f from Framework f inner join User u on f.author.id = u.id where u.id = :userId", Framework.class);
        query.setParameter("userId", userId);
        return Optional.of(query.getResultList().size());
    }

//    @Override
//    public List<Framework> search(String toSearch, List<FrameworkCategories> categories, List<FrameworkType> types, Integer starsLeft, Integer starsRight, boolean nameFlag, Integer commentAmount, Timestamp lastComment, Timestamp lastUpdated, Integer order, long page, long pageSize) {
//
//        CriteriaBuilder cb = em.getCriteriaBuilder();
//        CriteriaQuery<Framework> cQuery = cb.createQuery(Framework.class);
//        Root<Framework> root = cQuery.from(Framework.class);
//        cQuery.select(root);
//
//        if(toSearch==null && categories==null && types == null && starsLeft == 0 && starsRight == 5 && commentAmount==0 && lastComment==null && lastUpdated==null && (order==null||order==0)) {
//            final TypedQuery<Framework> query = em.createQuery(cQuery);
//            return query.getResultList();
//        }
//
//        List<Predicate> predicates = new ArrayList<>();
//
//        if(toSearch!=null && !toSearch.isEmpty()){
//            final String searchValue = "%"+toSearch.toLowerCase()+"%";
//            if(nameFlag || toSearch.length() < 3) {
//                predicates.add(cb.like(root.get("name"), searchValue));
//            }
//            else {
//                Predicate namePredicate = cb.like(root.get("name"), searchValue);
//                Predicate descriptionPredicate= cb.like(root.get("description"), searchValue);
//                Predicate introductionPredicate= cb.like(root.get("introduction"), searchValue);
//                predicates.add(cb.or(namePredicate, descriptionPredicate, introductionPredicate));
//            }
//        }
//
//        if(categories != null) {
//            predicates.add(root.get("category").in(categories));
//        }
//
//        if(types != null) {
//            predicates.add(root.get("type").in(types));
//        }
//
//        if(lastUpdated != null){
//            predicates.add(cb.greaterThanOrEqualTo(root.get("date"), lastUpdated));
//        }
//
//        cQuery.where(predicates);
//
//        if(order != null && order != 0){
//            switch (order){
//                case -1:
//                    cQuery.orderBy(cb.asc(root.get("stars")));
//                    break;
//                case 1:
//                    cQuery.orderBy(cb.desc(root.get("stars")));
//                    break;
//                case -2:
//                    cQuery.orderBy(cb.asc(root.get("commentAmount")));
//                    break;
//                case 2:
//                    cQuery.orderBy(cb.desc(root.get("commentAmount")));
//                    break;
//                case -3:
//                    cQuery.orderBy(cb.asc(root.get("date")));
//                    break;
//                case 3:
//                    cQuery.orderBy(cb.desc(root.get("date")));
//                    break;
//                case -4:
//                    cQuery.orderBy(cb.asc(root.get("lastComment")));
//                    break;
//                case 4:
//                    cQuery.orderBy(cb.desc(root.get("lastComment")));
//                    break;
//            }
//        }
//
//        List<Expression<?>> expressions = new ArrayList<>();
//
//        expressions.add(root.get("stars"));
//        expressions.add(root.get("commentAmount"));
//
//        cQuery.groupBy(expressions);
//
//        List<Predicate> havingList = new ArrayList<>();
//
//        havingList.add(cb.ge(root.get("stars"), starsLeft));
//        havingList.add(cb.le(root.get("stars"), starsRight));
//        havingList.add(cb.ge(root.get("commentAmount"), commentAmount));
//
//        if(lastComment!=null) {
//            havingList.add(cb.ge(cb.max(root.get("date"))))
//        }
//
//        cQuery.having(havingList);
//
//            have=have.concat(" and max(c.tstamp)>='"+lastComment+"'");
//        return namedJdbcTemplate.query(SELECTION+aux+GROUP_BY+have+orderAux+" LIMIT " + pageSize +" OFFSET " + pageSize*(page-1),params,ROW_MAPPER);
//
//    }

    @Override
    public List<Framework> search(String toSearch, List<FrameworkCategories> categories, List<FrameworkType> types, Integer starsLeft, Integer starsRight, boolean nameFlag, Integer commentAmount, Timestamp lastComment, Timestamp lastUpdated, Integer order, long page, long pageSize) {

        





        final TypedQuery<Framework> query = em.createQuery("select f from Framework f inner join User u on f.author.id = u.id where u.id = :userId", Framework.class);
        return query.getResultList();
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

        if (picture.length > 0) {
            framework.setPicture(picture);
        }  else {
            framework.setPicture(framework.getPicture());
        }

        em.merge(framework);
        return Optional.of(framework);
    }

    @Override
    public void delete(long id) {
        em.remove(em.getReference(Framework.class, id));
    }

    @Override
    public Integer searchResultsNumber(String toSearch, List<FrameworkCategories> categories, List<FrameworkType> types, Integer starsLeft, Integer starsRight, boolean nameFlag, Integer commentAmount, Timestamp lastComment, Timestamp lastUpdated) {
        final List<Framework> resultList = search(toSearch, categories, types, starsLeft, starsRight, nameFlag, commentAmount, lastComment, lastUpdated,0, -1, -1);
        return resultList.size();
    }
}
