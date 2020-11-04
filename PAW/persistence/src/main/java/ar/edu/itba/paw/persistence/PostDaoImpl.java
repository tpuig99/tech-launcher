package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Comment;
import ar.edu.itba.paw.models.Framework;
import ar.edu.itba.paw.models.Post;
import ar.edu.itba.paw.models.User;
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
    public List<Post> getAll(long page, long pageSize) {

        Query pagingQuery = em.createNativeQuery("SELECT post_id FROM posts LIMIT " + String.valueOf(pageSize) + " OFFSET " + String.valueOf((page-1)*pageSize));
        @SuppressWarnings("unchecked")
        List<Long> resultList = ((List<Number>)pagingQuery.getResultList()).stream().map(Number::longValue).collect(Collectors.toList());

        if(!resultList.isEmpty()) {
            TypedQuery<Post> query = em.createQuery("from Post as p where p.postId in (:resultList)", Post.class);
            query.setParameter("resultList", resultList);
            query.setMaxResults((int)pageSize);
            query.setFirstResult((int) ((page-1)*pageSize));
            return query.getResultList();
        }else{
            return new ArrayList<>();
        }
    }

    @Override
    public List<Post> getPostsByUser(long userId, long page, long pageSize) {
        Query pagingQuery = em.createNativeQuery("SELECT post_id FROM posts WHERE user_id = " + String.valueOf(userId)+ " LIMIT " + String.valueOf(pageSize) + " OFFSET " + String.valueOf((page-1)*pageSize));
        @SuppressWarnings("unchecked")
        List<Long> resultList = ((List<Number>)pagingQuery.getResultList()).stream().map(Number::longValue).collect(Collectors.toList());

        if(!resultList.isEmpty()) {
            TypedQuery<Post> query = em.createQuery("from Post as p where p.postId in (:resultList)", Post.class);
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
}
