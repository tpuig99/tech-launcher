package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

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
                + "FOREIGN KEY(reference) REFERENCES comments,"
                + ")");

//        insert into users values(1);
//        insert into frameworks values(1);
//        insert into comments values();
    }

    @Override
    public List<Comment> getCommentsByFramework(long frameworkId) {
        final List<Comment> toReturn = jdbcTemplate.query("SELECT * FROM comments where framework_id = ?", ROW_MAPPER, frameworkId);

        if (toReturn.isEmpty()) {
            toReturn.add(new Comment(1,1,1,"Sample Comment",5,4,new Timestamp(System.currentTimeMillis()), -1));
        }

        return toReturn;
    }

    @Override
    public List<Comment> getCommentsByFrameworkAndUser(long frameworkId, long userId) {
        final List<Comment> toReturn = jdbcTemplate.query("SELECT * FROM comments WHERE framework_id = ? AND user_id = ?", ROW_MAPPER, frameworkId, userId);

        if (toReturn.isEmpty()) {
            toReturn.add(new Comment(1,1,1,"Sample Comment",5,4,new Timestamp(System.currentTimeMillis()), -1));
        }

        return toReturn;
    }

    @Override
    public List<Comment> getCommentsByUser(long userId) {
        final List<Comment> toReturn = jdbcTemplate.query("SELECT * FROM comments WHERE user_id = ?", ROW_MAPPER, userId);

        if (toReturn.isEmpty()) {
            toReturn.add(new Comment(1,1,1,"Sample Comment",5,4,new Timestamp(System.currentTimeMillis()), -1));
        }

        return toReturn;
    }

    @Override
    public Comment insertComment(long frameworkId, long userId, String description, long reference) {
        Long millis = System.currentTimeMillis();
        final List<Comment> toReturn = jdbcTemplate.query("insert into comments values(DEFAULT, ? , ? , ? , 0, 0, ? , ?);", ROW_MAPPER,
                frameworkId, userId, description, millis, reference);
        return toReturn.get(0);
    }

    @Override
    public Comment deleteComment(long commentId) {
        final List<Comment> toReturn = jdbcTemplate.query("DELETE FROM comments WHERE comment_id = ? RETURNING *", ROW_MAPPER, commentId);
        return toReturn.get(0);
    }

    @Override
    public Comment changeComment(long commentId, String description) {
        final List<Comment> toReturn = jdbcTemplate.query("UPDATE comments SET description = ? WHERE comment_id = ? RETURNING *", ROW_MAPPER, description, commentId);
        return toReturn.get(0);
    }
}
