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
        final TypedQuery<Framework> query = em.createQuery("select f from Framework f where f.category= :category order by f.id", Framework.class);
        query.setParameter("category", category);
        query.setFirstResult((int) ((page-1) * pageSize));
        query.setMaxResults((int) pageSize);
        return query.getResultList();
    }

    @Override
    public List<Framework> getAll() {
        final TypedQuery<Framework> query = em.createQuery("select f from Framework f", Framework.class);
        return query.getResultList();
    }

    /* TODO: when Content is ready, update this query */
    @Override
    public List<Framework> getUserInterests(long userId) {
        final TypedQuery<Framework> query = em.createQuery("select distinct f from Framework f inner join Content c on f.id=c.framework.id where c.user.id = :userId", Framework.class);
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    @Override
    public List<Framework> getByMinStars(int stars) {
        final TypedQuery<Framework> query = em.createQuery("select f from Framework f left outer join FrameworkVote v on f.id = v.framework.id group by f having coalesce(avg(v.stars), 0) >= :stars", Framework.class);
        Double st = Double.valueOf(stars);
        query.setParameter("stars", st);
        return query.getResultList();
    }


    /* TODO: add pagination  */
    @Override
    public List<Framework> getByUser(long userId, long page, long pageSize) {
        final TypedQuery<Framework> query = em.createQuery("select f from Framework f where f.author.id = :userId order by f.id", Framework.class);
        query.setParameter("userId", userId);
        query.setFirstResult((int) ((page-1) * pageSize));
        query.setMaxResults((int) pageSize);
        return query.getResultList();
    }

    @Override
    public int getAmountByCategory(FrameworkCategories categories) {
        final TypedQuery<Framework> query = em.createQuery("from Framework f where category = :category", Framework.class);
        query.setParameter("category", categories);
        return query.getResultList().size();
    }

    @Override
    public Optional<Integer> getByUserCount(long userId) {
        final TypedQuery<Framework> query = em.createQuery("select f from Framework f left outer join User u on f.author.id = u.id where u.id = :userId", Framework.class);
        query.setParameter("userId", userId);
        return Optional.of(query.getResultList().size());
    }

    @Override
    public List<Framework> search(String toSearch, List<FrameworkCategories> categories, List<FrameworkType> types, Integer starsLeft, Integer starsRight, boolean nameFlag, Integer commentAmount, Timestamp lastComment, Timestamp lastUpdated, Integer order, long page, long pageSize) {

        if(toSearch == null && categories == null && types == null && starsLeft == 0 && starsRight == 5 && commentAmount == 0
                && lastComment == null && lastUpdated == null && (order == null || order == 0)) {

            if (page == -1 && pageSize == -1) {
                return getAll();
            }

            final TypedQuery<Framework> query = em.createQuery("select f from Framework f", Framework.class);
            query.setFirstResult((int) ((page-1) * pageSize));
            query.setMaxResults((int) pageSize);
            return query.getResultList();
        }

        StringBuilder sb = new StringBuilder(" select f from Framework f left outer join f.frameworkVotes v on f.id = v.framework.id left outer join f.comments c on f.id = c.framework.id");
    //TypedQuery<Framework> query2 = em.createQuery("select f from Framework f left outer join f.frameworkVotes v on f.id = v.framework.id left outer join f.comments c on f.id = c.framework.id", Framework.class);
        if(toSearch!=null || categories!=null || types != null || lastUpdated!=null) {
            sb.append(" where ");
        }

        String search = "";

        if(toSearch != null && !toSearch.isEmpty()){
            search = "%"+toSearch.toLowerCase()+"%";
            if(nameFlag || toSearch.length() < 3) {
                sb.append(" lower(f.name) like :search ");
            }
            else {
                sb.append(" lower(f.name) like :search ");
                sb.append(" or lower(f.description) like :search ");
                sb.append(" or lower(f.introduction) like :search ");
            }
       }

        if (categories != null) {
            if(!sb.toString().substring(sb.length()-6).contains("where")){
                sb.append(" and ");
            }
            sb.append(" f.category in (:categories) ");
        }

        if (types != null) {
            if(!sb.toString().substring(sb.length()-6).contains("where")){
                sb.append(" and ");
            }
          /*  if(!sb.toString().contains("where")) {
                sb.append(" and ");
            }*/
            sb.append(" f.type in (:types) ");
        }

        if (lastUpdated != null) {
            if(!sb.toString().substring(sb.length()-6).contains("where")){
                sb.append(" and ");
            }
            sb.append(" f.publishDate >= :lastUpdated ");
        }



        sb.append(" group by f having coalesce(avg(v.stars),0) >= :starsLeft and coalesce(avg(v.stars),0) <= :starsRight and count(distinct c.commentId) >= :commentAmount ");

        if(lastComment!=null) {
            sb.append(" and max(c.timestamp) >= :lastComment ");
        }

        if(order != null && order != 0){
            sb.append(" order by ");

            switch (Math.abs(order)){
                case 1:
                    sb.append(" coalesce(avg(v.stars),0) ").append(order > 0 ?" desc ":" asc ");
                    break;
                case 2:
                    sb.append(" count(distinct c.commentId) ").append(order > 0 ?" desc ":" asc ");
                    break;
                case 3:
                    sb.append(" f.publishDate ").append(order > 0 ?" desc ":" asc ");
                    break;
                case 4:
                    sb.append(" max(c.timestamp) ").append(order > 0 ?" desc ":" asc ");
                    break;
            }
        }

        final TypedQuery<Framework> query = em.createQuery(sb.toString(), Framework.class);
        if (page != -1 || pageSize != -1) {
            query.setFirstResult((int) ((page-1) * pageSize));
            query.setMaxResults((int) pageSize);
        }
        if(toSearch != null && !toSearch.isEmpty())
            query.setParameter("search", search);
        if(categories != null)
            query.setParameter("categories", categories);
        if(types != null)
            query.setParameter("types", types);
        if(lastUpdated != null)
            query.setParameter("lastUpdated", lastUpdated);
        query.setParameter("starsLeft", Double.valueOf(starsLeft));
        query.setParameter("starsRight", Double.valueOf(starsRight));
        query.setParameter("commentAmount", Long.valueOf(commentAmount));
        if(lastComment != null)
            query.setParameter("lastComment", lastComment);

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
