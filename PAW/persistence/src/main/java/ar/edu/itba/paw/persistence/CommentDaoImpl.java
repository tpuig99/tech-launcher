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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class CommentDaoImpl implements CommentDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final static RowMapper<Comment> ROW_MAPPER = CommentDaoImpl::mapRow;
    private final static RowMapper<Comment> ROW_MAPPER_PROFILE = CommentDaoImpl::mapRow2;

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
                rs.getLong("reference")
        );
    }

    private static Comment mapRow2(ResultSet rs, int rowNum) throws SQLException {
        return new Comment(rs.getLong("comment_id"),
                rs.getInt("framework_id"),
                rs.getLong("user_id"),
                rs.getString("description"),
                rs.getTimestamp("tstamp"),
                rs.getLong("reference"),
                rs.getString("framework_name")
        );
    }

    @Override
    public Optional<Comment> getById(long contentId) {
        return jdbcTemplate.query("SELECT * FROM comments WHERE comment_id = ?", new Object[] {contentId},ROW_MAPPER).stream().findFirst();
    }

    @Override
    public List<Comment> getCommentsByFramework(long frameworkId) {
        return jdbcTemplate.query("SELECT * FROM comments where framework_id = ?", new Object[] {frameworkId},  ROW_MAPPER );
    }

    @Override
    public List<Comment> getCommentsByFrameworkAndUser(long frameworkId, long userId) {
        return jdbcTemplate.query("SELECT * FROM comments WHERE framework_id = ? AND user_id = ?", new Object[] {frameworkId, userId},  ROW_MAPPER);
    }

    @Override
    public List<Comment> getCommentsByUser(long userId) {
        return jdbcTemplate.query("select comment_id, frameworks.framework_id,user_id,comments.description,tstamp,reference,framework_name from comments join frameworks on frameworks.framework_id = comments.framework_id where user_id=?",
                new Object[] {userId}, ROW_MAPPER_PROFILE);
    }



    @Override
    public Comment insertComment(long frameworkId, long userId, String description, Long reference) {
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        final Map<String, Object> args = new HashMap<>();
        args.put("framework_id", frameworkId);
        args.put("user_id", userId);
        args.put("description", description);
        args.put("tstamp", ts);
        args.put("reference", reference);

        final Number voteId = jdbcInsert.executeAndReturnKey(args);
        return new Comment (voteId.longValue(), frameworkId, userId, description, ts, reference);
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
