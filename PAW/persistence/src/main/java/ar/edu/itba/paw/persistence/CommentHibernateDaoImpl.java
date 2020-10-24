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
            value = SELECTION+USER_VOTE+FROM+"where c.framework_id = ? AND c.reference IS NULL"+GROUP_BY + " LIMIT ? OFFSET ?";
            return jdbcTemplate.query(value, new Object[] {userId,frameworkId, pageSize, (page-1)*pageSize},  SET_EXTRACTOR_USER_VOTE );
        }
        value = SELECTION+FROM+"where c.framework_id = ? AND c.reference IS NULL"+GROUP_BY + " LIMIT ? OFFSET ?";
        return jdbcTemplate.query(value, new Object[] {frameworkId, pageSize, (page-1)*pageSize},  SET_EXTRACTOR);
    }

    public List<Comment> getCommentsWithoutReferenceByFrameworkWithUser(long frameworkId,Long userId) {
        String value;
        if(userId!=null)
        {
            value = SELECTION+USER_VOTE+FROM+"where c.framework_id = ? AND c.reference IS NULL"+GROUP_BY + " LIMIT ? OFFSET ?";
            return jdbcTemplate.query(value, new Object[] {userId,frameworkId},  SET_EXTRACTOR_USER_VOTE );
        }
        value = SELECTION+FROM+"where c.framework_id = ? AND c.reference IS NULL"+GROUP_BY;
        return jdbcTemplate.query(value, new Object[] {frameworkId},  SET_EXTRACTOR);
    }

    @Override
    public List<Comment> getCommentsByFrameworkAndUser(long frameworkId, long userId) {
        String value = SELECTION+FROM+"WHERE c.framework_id = ? AND c.user_id = ?"+GROUP_BY;
        return jdbcTemplate.query(value, new Object[] {frameworkId, userId},  SET_EXTRACTOR);
    }

    @Override
    public List<Comment> getCommentsByUser(long userId, long page, long pageSize ) {
        String value = SELECTION +FROM+"WHERE c.user_id = ?"+GROUP_BY + " LIMIT ? OFFSET ?" ;
        return jdbcTemplate.query(value, new Object[] {userId, pageSize, (page-1)*pageSize}, SET_EXTRACTOR);
    }

    @Override
    public Optional<Integer> getCommentsCountByUser(long userId){
        return jdbcTemplate.query("select count (*) from comments inner join users on comments.user_id = users.user_id where comments.user_id = ?", new Object[] {userId}, ROW_MAPPER_COUNT).stream().findFirst();
    }

    //TODO optimize queries
    @Override
    public Map<Long, List<Comment>> getRepliesByFramework(long frameworkId) {
        Map<Long, List<Comment>> toReturn = new HashMap<>();
        List<Comment> replies = new ArrayList<>();
        long commentId;

        List<Comment> commentsWithoutRef = getCommentsWithoutReferenceByFrameworkWithUser(frameworkId,null);

        if(!commentsWithoutRef.isEmpty()) {
            String value = SELECTION+FROM+"where c.framework_id = ? AND c.reference = ?"+GROUP_BY;
            for (Comment comment : commentsWithoutRef) {
                commentId = comment.getCommentId();
                toReturn.put(commentId, jdbcTemplate.query(value, new Object[]{frameworkId, commentId}, SET_EXTRACTOR));

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
