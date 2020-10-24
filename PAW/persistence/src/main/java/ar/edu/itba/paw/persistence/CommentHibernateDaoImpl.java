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
    public Comment insertComment(long framework_id, long user_id, String description, Long reference) {
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        final Comment comment = new Comment(em.getReference(Framework.class, framework_id), em.getReference(User.class, user_id), description, VOTES_DEFAULT, VOTES_DEFAULT, ts, reference);

        em.persist(comment);
        return comment;
    }


    private static List<Comment> extractor(ResultSet rs) throws SQLException {
        List<Comment> list=new ArrayList<>();
        Long currentComment = null;
        Comment rc = null;
        while(rs.next()){
            Long nextComment=rs.getLong("comment_id");
            if(currentComment==null||currentComment!=nextComment){
                if(rc!=null){
                    list.add(rc);
                }
                currentComment = nextComment;
                rc = new Comment(rs.getLong("comment_id"),
                        rs.getInt("framework_id"),
                        rs.getLong("user_id"),
                        rs.getString("description"),
                        rs.getInt("pos"),
                        rs.getInt("neg"),
                        rs.getTimestamp("tstamp"),
                        rs.getLong("reference"),
                        rs.getString("framework_name"),
                        rs.getString("user_name"),
                        FrameworkCategories.getByName(rs.getString("category")),
                        rs.getBoolean("is_verify"),
                        rs.getBoolean("is_admin")
                );
                String report =rs.getString("user_name_reporter");
                if(report!=null)
                    rc.addReporter(report);
            }else{
                rc.addReporter(rs.getString("user_name_reporter"));
            }
        }
        if(rc!=null){
            list.add(rc);
        }
        return list;
    }

    private static List<Comment> extractorUserVote(ResultSet rs) throws SQLException {
        List<Comment> list=new ArrayList<>();
        Long currentComment = null;
        Comment rc = null;
        while(rs.next()){
            Long nextComment=rs.getLong("comment_id");
            if(currentComment==null||currentComment!=nextComment){
                if(rc!=null){
                    list.add(rc);
                }
                currentComment = nextComment;
                rc = new Comment(rs.getLong("comment_id"),
                        rs.getInt("framework_id"),
                        rs.getLong("user_id"),
                        rs.getString("description"),
                        rs.getInt("pos"),
                        rs.getInt("neg"),
                        rs.getTimestamp("tstamp"),
                        rs.getLong("reference"),
                        rs.getString("framework_name"),
                        rs.getString("user_name"),
                        FrameworkCategories.getByName(rs.getString("category")),
                        rs.getBoolean("is_verify"),
                        rs.getBoolean("is_admin"),
                        rs.getInt("user_vote")
                );
                String report =rs.getString("user_name_reporter");
                if(report!=null)
                    rc.addReporter(report);
            }else{
                rc.addReporter(rs.getString("user_name_reporter"));
            }
        }
        if(rc!=null){
            list.add(rc);
        }
        return list;
    }

    private static Integer mapRowCount(ResultSet rs, int i) throws SQLException {
        return rs.getInt("count");
    }

    private static Comment mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Comment(rs.getLong("comment_id"),
                rs.getInt("framework_id"),
                rs.getLong("user_id"),
                rs.getString("description"),
                rs.getInt("pos"),
                rs.getInt("neg"),
                rs.getTimestamp("tstamp"),
                rs.getLong("reference"),
                rs.getString("framework_name"),
                rs.getString("user_name"),
                FrameworkCategories.getByName(rs.getString("category")),
                rs.getBoolean("is_verify"),
                rs.getBoolean("is_admin")
        );
    }

    private static Comment mapRowUserAuthVote(ResultSet rs, int rowNum) throws SQLException {
        return new Comment(rs.getLong("comment_id"),
                rs.getInt("framework_id"),
                rs.getLong("user_id"),
                rs.getString("description"),
                rs.getInt("pos"),
                rs.getInt("neg"),
                rs.getTimestamp("tstamp"),
                rs.getLong("reference"),
                rs.getString("framework_name"),
                rs.getString("user_name"),
                FrameworkCategories.getByName(rs.getString("category")),
                rs.getBoolean("is_verify"),
                rs.getBoolean("is_admin"),
                rs.getInt("user_vote")
        );
    }


    @Override
    public Optional<Comment> getById(long commentId) {
        return Optional.ofNullable(em.find(Comment.class, commentId));
    }

    @Override
    public List<Comment> getCommentsByFramework(long frameworkId,Long userId) {
        String value;
        if(userId!=null)
        {
            value = SELECTION+USER_VOTE+FROM+"where c.framework_id = ?"+GROUP_BY;
            return jdbcTemplate.query(value, new Object[] {userId,frameworkId},SET_EXTRACTOR_USER_VOTE  );
        }
        value = SELECTION+FROM+"where c.framework_id = ?"+GROUP_BY;
        return jdbcTemplate.query(value, new Object[] {frameworkId},  SET_EXTRACTOR );
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
