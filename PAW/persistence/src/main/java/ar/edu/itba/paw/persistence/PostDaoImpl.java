package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.*;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class PostDaoImpl implements PostDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<Post> findById(long postId) {
        return Optional.ofNullable(em.find(Post.class, postId));
    }

    @Override
    public List<Post> getAll() {
        TypedQuery<Post> query = em.createQuery("from Post as p", Post.class);
        return query.getResultList();
    }

    @Override
    public List<Post> getAllByPage(long page, long pageSize) {
            TypedQuery<Post> query = em.createQuery("from Post as p", Post.class);
            query.setMaxResults((int)pageSize);
            query.setFirstResult((int) ((page-1)*pageSize));
            return query.getResultList();
    }

    @Override
    public List<Post> getPostsByUser(long userId, long page, long pageSize) {
        Query pagingQuery = em.createNativeQuery("SELECT post_id FROM posts WHERE user_id = " + String.valueOf(userId)+ " LIMIT " + String.valueOf(pageSize) + " OFFSET " + String.valueOf((page-1)*pageSize));
        @SuppressWarnings("unchecked")
        List<Long> resultList = ((List<Number>)pagingQuery.getResultList()).stream().map(Number::longValue).collect(Collectors.toList());

        if(!resultList.isEmpty()) {
            TypedQuery<Post> query = em.createQuery("select distinct p from Post as p where p.postId in (:resultList)", Post.class);
            query.setParameter("resultList", resultList);
            return query.getResultList();
        }else{
            return new ArrayList<>();
        }
    }

    @Override
    public Optional<Integer> getPostsCountByUser(long userId){
        final TypedQuery<Post> query = em.createQuery("from Post as p where p.user.id = :userId", Post.class);
        query.setParameter("userId", userId);
        return Optional.of(query.getResultList().size());
    }

    @Override
    public List<Post> getByTagName(String tagName, long page, long pageSize) {
        Query pagingQuery = em.createNativeQuery("SELECT DISTINCT p.post_id FROM posts p natural JOIN post_tags t  WHERE t.tag_name = " + String.valueOf(tagName)+ " LIMIT " + String.valueOf(pageSize) + " OFFSET " + String.valueOf((page-1)*pageSize));
        @SuppressWarnings("unchecked")
        List<Long> resultList = ((List<Number>)pagingQuery.getResultList()).stream().map(Number::longValue).collect(Collectors.toList());

        if(!resultList.isEmpty()) {
            TypedQuery<Post> query = em.createQuery("select p from Post as p inner join PostTag t on p.postId = t.post.postId where p.postId in (:resultList)", Post.class);
            query.setParameter("resultList", resultList);
            return query.getResultList();
        }else{
            return new ArrayList<>();
        }
    }

    @Override
    public Post insertPost(long userId, String title, String description) {
        Date ts = new Date(System.currentTimeMillis());
        final Post post = new Post();

        post.setUser(em.getReference(User.class, userId));
        post.setTitle(title);
        post.setTimestamp(ts);
        post.setDescription(description);

        em.persist(post);
        return post;
    }

    @Override
    public void deletePost(long postId) {
        em.remove(em.getReference(Post.class,postId));
    }

    @Override
    public Optional<Post> update(long postId, String title, String description) {
        Post post = em.find(Post.class, postId);

        post.setTitle(title);
        post.setDescription(description);

        em.merge(post);
        return Optional.of(post);
    }

    @Override
    public int getAmount() {
        Query pagingQuery = em.createNativeQuery("SELECT post_id FROM posts");
        @SuppressWarnings("unchecked")
        List<Long> resultList = ((List<Number>)pagingQuery.getResultList()).stream().map(Number::longValue).collect(Collectors.toList());

        if(!resultList.isEmpty()) {
            TypedQuery<Post> query = em.createQuery("from Post as p where p.postId in (:resultList)", Post.class);
            query.setParameter("resultList", resultList);
            return query.getResultList().size();
        }else{
            return 0;
        }
    }

    @Override
    public List<Post> search(String toSearch, List<String> tags, Integer starsLeft, Integer starsRight, Integer commentAmount, Date lastComment, Date lastUpdated, Integer order, long page, long pageSize) {
        if(toSearch == null && tags == null && commentAmount == 0
                && lastComment == null && lastUpdated == null && (order == null || order == 0)) {
            if (page == -1 && pageSize == -1) {
                return getAll();
            }

            return getAllByPage(page,pageSize);
        }
        StringBuilder sb = new StringBuilder(" select p from Post p left outer join p.postVotes v on p.postId = v.post.postId left outer join p.postComments c on p.postId = c.post.postId left outer join p.postTags t on p.postId = t.post.postId ");
        if(toSearch!=null || tags!=null || lastUpdated!=null) {
            sb.append(" where ");
        }

        String search = "";

        if(toSearch != null && !toSearch.isEmpty()){
            search = "%"+toSearch.toLowerCase()+"%";
                sb.append(" lower(p.description) like :search or lower(p.title) like :search or lower(t.tagName) like :search ");
        }

        if (tags != null) {
            if(!sb.toString().substring(sb.length()-6).contains("where")){
                sb.append(" and ");
            }
            if(search != ""){
                sb.append(" (  t.tagName in (:tags) or lower(t.tagName) like :search ) ");
            } else {
                sb.append(" t.tagName in (:tags) ");
            }
        }

        if (lastUpdated != null) {
            if(!sb.toString().substring(sb.length()-6).contains("where")){
                sb.append(" and ");
            }
            sb.append(" p.timestamp >= :lastUpdated ");
        }

        sb.append(" group by p having count(distinct c.postCommentId) >= :commentAmount ");

        if(lastComment!=null) {
            sb.append(" and max(c.timestamp) >= :lastComment ");
        }

        if(order != null && order != 0 && Math.abs(order) != 1){
            sb.append(" order by ");

            switch (Math.abs(order)){
                case 2:
                    sb.append(" count(distinct c.postCommentId) ").append(order > 0 ?" desc ":" asc ");
                    break;
                case 3:
                    sb.append(" p.timestamp ").append(order > 0 ?" desc ":" asc ");
                    break;
                case 4:
                    sb.append(" max(c.timestamp) ").append(order > 0 ?" desc ":" asc ");
                    break;
            }
        }

        final TypedQuery<Post> query = em.createQuery(sb.toString(), Post.class);
        if (page != -1 || pageSize != -1) {
            query.setFirstResult((int) ((page-1) * pageSize));
            query.setMaxResults((int) pageSize);
        }
        query.setParameter("commentAmount", Long.valueOf(commentAmount));
        if(toSearch != null && !toSearch.isEmpty())
            query.setParameter("search", search);
        if(tags != null)
            query.setParameter("tags", tags);
        if(lastUpdated != null)
            query.setParameter("lastUpdated", lastUpdated);
//        query.setParameter("starsLeft", Double.valueOf(starsLeft));
//        query.setParameter("starsRight", Double.valueOf(starsRight));
        if(lastComment != null)
            query.setParameter("lastComment", lastComment);

        return query.getResultList();

    }

    @Override
    public Integer searchResultsNumber(String toSearch, List<String> tags, Integer starsLeft, Integer starsRight, Integer commentAmount, Date lastComment, Date lastUpdated, Integer order) {
        final List<Post> resultList = search(toSearch, tags, starsLeft, starsRight, commentAmount, lastComment, lastUpdated, order, -1, -1);
        return resultList.size();
    }
}
