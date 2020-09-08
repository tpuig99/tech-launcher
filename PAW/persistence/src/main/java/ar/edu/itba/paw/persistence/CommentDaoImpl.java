package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Comment;
import ar.edu.itba.paw.models.Content;
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

@Repository
public class CommentDaoImpl implements CommentDao {
    private JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final static RowMapper<Comment> ROW_MAPPER = new
            RowMapper<Comment>() {
                @Override
                public Comment mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return new Comment(rs.getLong("comment_id"),
                            rs.getInt("framework_id"),
                            rs.getLong("user_id"),
                            rs.getString("description"),
                            rs.getLong("votes_up"),
                            rs.getLong("votes_down"),
                            rs.getTimestamp("tstamp"),
                            rs.getLong("reference")
                            );
                }
            };

    @Autowired
    public CommentDaoImpl(final DataSource ds) {
        this.jdbcTemplate = new JdbcTemplate(ds);

        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("comments")
                .usingGeneratedKeyColumns("comment_id");

        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS comments ("
                + "comment_id SERIAL PRIMARY KEY,"
                + "framework_id int NOT NULL,"
                + "user_id int NOT NULL,"
                + "description varchar(500) NOT NULL,"
                + "votes_up int,"
                + "votes_down int,"
                + "tstamp timestamp NOT NULL,"
                + "reference int,"
                + "FOREIGN KEY(framework_id) REFERENCES frameworks,"
                + "FOREIGN KEY(user_id) REFERENCES users,"
                + "FOREIGN KEY(reference) REFERENCES comments"
                + ")");

    }

    @Override
    public Comment getById(long contentId) {
        final List<Comment> toReturn = jdbcTemplate.query("SELECT * FROM comments WHERE comment_id = ?", ROW_MAPPER, contentId);
        if (toReturn.isEmpty()) {
            return null;
        }
        return toReturn.get(0);
    }

    @Override
    public List<Comment> getCommentsByFramework(long frameworkId) {
        final List<Comment> toReturn = jdbcTemplate.query("SELECT * FROM comments where framework_id = ?", ROW_MAPPER, frameworkId);

        if (toReturn.isEmpty()) {
            return null;
        }

        return toReturn;
    }

    @Override
    public List<Comment> getCommentsByFrameworkAndUser(long frameworkId, long userId) {
        final List<Comment> toReturn = jdbcTemplate.query("SELECT * FROM comments WHERE framework_id = ? AND user_id = ?", ROW_MAPPER, frameworkId, userId);

        if (toReturn.isEmpty()) {
            return null;
        }

        return toReturn;
    }

    @Override
    public List<Comment> getCommentsByUser(long userId) {
        final List<Comment> toReturn = jdbcTemplate.query("SELECT * FROM comments WHERE user_id = ?", ROW_MAPPER, userId);

        if (toReturn.isEmpty()) {
            return null;
        }

        return toReturn;
    }

    @Override
    public Comment insertComment(long frameworkId, long userId, String description, long reference) {
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        final Map<String, Object> args = new HashMap<>();
        args.put("framework_id", frameworkId);
        args.put("user_id", userId);
        args.put("description", description);
        args.put("votes_up", 0);
        args.put("votes_down", 0);
        args.put("tstamp", ts);
        args.put("reference", reference);

        final Number voteId = jdbcInsert.executeAndReturnKey(args);
        return new Comment (voteId.longValue(), frameworkId, userId, description, 0, 0, ts, reference);
    }

    @Override
    public int deleteComment(long commentId) {
        return jdbcTemplate.update("DELETE FROM content WHERE comment_id = ?", commentId);
    }

    @Override
    public Comment changeComment(long commentId, String description) {
        jdbcTemplate.update("UPDATE comments SET description = ? WHERE comment_id = ?", description, commentId);
        return getById(commentId);
    }
}
