package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

@Repository
public class CommentDaoImpl implements CommentDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final String SELECTION ="select comments.comment_id,comments.framework_id,comments.user_id,comments.description,tstamp,reference,framework_name,count(case when vote=-1 then vote end) as neg,count(case when vote=1 then vote end) as pos from comments left join comment_votes cv on comments.comment_id = cv.comment_id left join frameworks on comments.framework_id = frameworks.framework_id ";
    private final String GROUP_BY = " group by comments.comment_id , framework_name";
    private final static RowMapper<Comment> ROW_MAPPER = CommentDaoImpl::mapRow;

    @Autowired
    public CommentDaoImpl(final DataSource ds) {
        this.jdbcTemplate = new JdbcTemplate(ds);

        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("comments")
                .usingGeneratedKeyColumns("comment_id");

    }

    private static Comment mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Comment(rs.getLong("comment_id"),
                rs.getInt("framework_id"),
                rs.getLong("user_id"),
                rs.getString("description"),
                rs.getTimestamp("tstamp"),
                rs.getLong("reference"),
                rs.getString("framework_name"),
                rs.getInt("pos"),
                rs.getInt("neg")
        );
    }

   /* private static Long mapRow3(ResultSet rs, int rowNum) throws SQLException {
        return rs.getLong("comment_id")
        );
    }*/

    @Override
    public Optional<Comment> getById(long commentId) {
        String value = SELECTION+"WHERE comments.comment_id = ?"+GROUP_BY;
        return jdbcTemplate.query(value, new Object[] {commentId},ROW_MAPPER).stream().findFirst();
    }

    @Override
    public List<Comment> getCommentsByFramework(long frameworkId) {
        String value = SELECTION+"where comments.framework_id = ?"+GROUP_BY;
        return jdbcTemplate.query(value, new Object[] {frameworkId},  ROW_MAPPER );
    }

    @Override
    public List<Comment> getCommentsWithoutReferenceByFramework(long frameworkId) {
        String value = SELECTION+"where comments.framework_id = ? AND comments.reference IS NULL"+GROUP_BY;
        return jdbcTemplate.query(value, new Object[] {frameworkId},  ROW_MAPPER );
    }

    @Override
    public List<Comment> getCommentsByFrameworkAndUser(long frameworkId, long userId) {
        String value = SELECTION+"WHERE comments.framework_id = ? AND comments.user_id = ?"+GROUP_BY;
        return jdbcTemplate.query(value, new Object[] {frameworkId, userId},  ROW_MAPPER);
    }

    @Override
    public List<Comment> getCommentsByUser(long userId) {
        String value = SELECTION+"WHERE comments.user_id = ?"+GROUP_BY;
        return jdbcTemplate.query(value, new Object[] {userId}, ROW_MAPPER);
    }

    //TODO optimize queries
    @Override
    public Map<Long, List<Comment>> getRepliesByFramework(long frameworkId) {
        Map<Long, List<Comment>> toReturn = new HashMap<>();
        List<Comment> replies = new ArrayList<>();
        long commentId;

        List<Comment> commentsWithoutRef = getCommentsWithoutReferenceByFramework(frameworkId);

        if(!commentsWithoutRef.isEmpty()) {
            String value = SELECTION+"where comments.framework_id = ? AND comments.reference = ?"+GROUP_BY;
            for (Comment comment : commentsWithoutRef) {
                commentId = comment.getCommentId();
                toReturn.put(commentId, jdbcTemplate.query(value, new Object[]{frameworkId, commentId}, ROW_MAPPER));

            }
        }
        return toReturn;
    }


    @Override
    public Optional<Comment> insertComment(long frameworkId, long userId, String description, Long reference) {
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        final Map<String, Object> args = new HashMap<>();
        args.put("framework_id", frameworkId);
        args.put("user_id", userId);
        args.put("description", description);
        args.put("tstamp", ts);
        args.put("reference", reference);

        final Number commentId = jdbcInsert.executeAndReturnKey(args);
        return getById(commentId.longValue());
    }

    @Override
    public int deleteComment(long commentId) {
        return jdbcTemplate.update("DELETE FROM content WHERE comment_id = ?", commentId);
    }

    @Override
    public Optional<Comment> changeComment(long commentId, String description) {
        jdbcTemplate.update("UPDATE comments SET description = ? WHERE comment_id = ?", description, commentId);
        return getById(commentId);
    }

}
