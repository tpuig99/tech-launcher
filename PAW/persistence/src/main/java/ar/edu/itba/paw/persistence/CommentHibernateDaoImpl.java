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
import javax.persistence.TypedQuery;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

@Repository
public class CommentHibernateDaoImpl implements CommentDao {
     static private final Integer VOTES_DEFAULT = 0;

    @PersistenceContext
    private EntityManager em;


    @Override
    public Comment insertComment(Framework framework, User user, String description, Long reference) {
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        final Comment comment = new Comment( framework, user, description, VOTES_DEFAULT, VOTES_DEFAULT, ts, reference);

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
        final TypedQuery<Comment> query = em.createQuery("from Comment as c where c.user.id = :userId", Comment.class);
        query.setParameter("userId", userId);
        query.setFirstResult((int) ((page-1) * pageSize));
        query.setMaxResults((int) pageSize);
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
        em.remove(em.getReference(VerifyUser.class,commentId));
    }

    @Override
    public Optional<Comment> changeComment(long commentId, String description) {

        Comment comment = em.find(Comment.class, commentId);
        comment.setDescription(description);
        em.merge(comment);
        return Optional.ofNullable(comment);
    }

}
