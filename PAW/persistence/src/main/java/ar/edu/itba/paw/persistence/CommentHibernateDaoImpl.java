package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Repository
public class CommentHibernateDaoImpl implements CommentDao {
     static private final Integer VOTES_DEFAULT = 0;

    @PersistenceContext
    private EntityManager em;


    @Override
    public Comment insertComment(long frameworkId, long userId, String description, Long reference) {
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        final Comment comment = new Comment();
        comment.setFramework(em.getReference(Framework.class, frameworkId));
        comment.setUser(em.getReference(User.class, userId));
        comment.setDescription(description);
        comment.setTimestamp(ts);
        comment.setReference(reference);
        em.persist(comment);
        return comment;
    }

    @Override
    public Optional<Comment> getById(long commentId) {
        return Optional.ofNullable(em.find(Comment.class, commentId));
    }

    @Override
    public List<Comment> getCommentsByFramework(long frameworkId,Long userId) {

        if(userId!=null)
        {
            final TypedQuery<Comment> query = em.createQuery("from Comment as c where c.user.id = :userId and c.framework.id = :frameworkId ", Comment.class);
            query.setParameter("userId", userId);
            query.setParameter("frameworkId", frameworkId);
            return query.getResultList();
        }
        final TypedQuery<Comment> query = em.createQuery("from Comment as c where c.framework.id = :frameworkId ", Comment.class);
        query.setParameter("frameworkId", frameworkId);

        return query.getResultList();
    }

    @Override
    public List<Comment> getCommentsWithoutReferenceByFrameworkWithUser(long frameworkId,Long userId, long page, long pageSize) {
        String value;
        if(userId!=null)
        {
            final TypedQuery<Comment> query = em.createQuery("from Comment as c where c.user.id = :userId and c.framework.id = :frameworkId and c.reference is null ", Comment.class);
            query.setParameter("frameworkId", frameworkId);
            query.setParameter("userId", userId);
            query.setFirstResult((int) ((page-1) * pageSize));
            query.setMaxResults((int) pageSize);
            return query.getResultList();
        }
        final TypedQuery<Comment> query = em.createQuery("from Comment as c where c.framework.id = :frameworkId and c.reference is null ", Comment.class);
        query.setParameter("frameworkId", frameworkId);
        query.setFirstResult((int) ((page-1) * pageSize));
        query.setMaxResults((int) pageSize);
        return query.getResultList();
    }

    public List<Comment> getCommentsWithoutReferenceByFrameworkWithUser(long frameworkId,Long userId) {
        String value;
        if(userId!=null)
        {

            final TypedQuery<Comment> query = em.createQuery("from Comment as c where c.user.id = :userId and c.framework.id = :frameworkId and c.reference is null ", Comment.class);
            query.setParameter("frameworkId", frameworkId);
            query.setParameter("userId", userId);
            return query.getResultList();
        }
        final TypedQuery<Comment> query = em.createQuery("from Comment as c where c.framework.id = :frameworkId and c.reference is null ", Comment.class);
        query.setParameter("frameworkId", frameworkId);
        return query.getResultList();
    }

    @Override
    public List<Comment> getCommentsByFrameworkAndUser(long frameworkId, long userId) {
        final TypedQuery<Comment> query = em.createQuery("from Comment as c where c.framework.id = :frameworkId and c.user.id = :userId", Comment.class);
        query.setParameter("frameworkId", frameworkId);
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    @Override
    public List<Comment> getCommentsByUser(long userId, long page, long pageSize ) {
        Query pagingQuery = em.createNativeQuery("SELECT comment_id FROM comments WHERE user_id = " + String.valueOf(userId)+ " LIMIT " + String.valueOf(pageSize) + " OFFSET " + String.valueOf((page-1)*pageSize));
        @SuppressWarnings("unchecked")
        List<Long> resultList = ((List<Number>)pagingQuery.getResultList()).stream().map(Number::longValue).collect(Collectors.toList());


        TypedQuery<Comment> query = em.createQuery("from Comment as c where c.commentId in (:resultList)", Comment.class);
        query.setParameter("resultList", resultList);
        return query.getResultList();
    }

    @Override
    public Optional<Integer> getCommentsCountByUser(long userId){
        final TypedQuery<Comment> query = em.createQuery("from Comment as c where c.user.id = :userId", Comment.class);
        query.setParameter("userId", userId);
        return Optional.of(query.getResultList().size());
    }

    //TODO optimize queries
    @Override
    public Map<Long, List<Comment>> getRepliesByFramework(long frameworkId) {
        Map<Long, List<Comment>> toReturn = new HashMap<>();
        List<Comment> replies = new ArrayList<>();
        long commentId;
        TypedQuery<Comment> query;


        List<Comment> commentsWithoutRef = getCommentsWithoutReferenceByFrameworkWithUser(frameworkId,null);

        if(!commentsWithoutRef.isEmpty()) {


            for (Comment comment : commentsWithoutRef) {
                commentId = comment.getCommentId();
                query = em.createQuery("from Comment as c where c.framework.id = :frameworkId AND c.reference = :commentId", Comment.class);
                query.setParameter("frameworkId", frameworkId);
                query.setParameter("commentId", commentId);
                toReturn.put(commentId, query.getResultList());

            }
        }
        return toReturn;
    }




    @Override
    public void deleteComment(long commentId) {
        em.remove(em.getReference(Comment.class,commentId));
    }

    @Override
    public Optional<Comment> changeComment(long commentId, String description) {

        Comment comment = em.find(Comment.class, commentId);
        comment.setDescription(description);
        em.merge(comment);
        return Optional.ofNullable(comment);
    }

}
