package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Content;
import ar.edu.itba.paw.models.ContentTypes;
import ar.edu.itba.paw.models.Vote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ContentDaoImpl implements ContentDao {
    private JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    private final static RowMapper<Content> ROW_MAPPER = new
            RowMapper<Content>() {
                @Override
                public Content mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return new Content(rs.getLong("content_id"),
                            rs.getInt("framework_id"),
                            rs.getLong("user_id"),
                            rs.getString("title"),
                            rs.getLong("votes_up"),
                            rs.getLong("votes_down"),
                            rs.getTimestamp("tstamp"),
                            rs.getURL(rs.getString("link")),
                            Enum.valueOf(ContentTypes.class, rs.getString("type"))
                    );
                }
            };

    @Autowired
    public ContentDaoImpl(final DataSource ds) {
        this.jdbcTemplate = new JdbcTemplate(ds);

        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("content")
                .usingGeneratedKeyColumns("content_id");

        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS content ("
                + "content_id SERIAL PRIMARY KEY,"
                + "framework_id int NOT NULL,"
                + "user_id int NOT NULL,"
                + "title varchar(100) NOT NULL,"
                + "votes_up int,"
                + "votes_down int,"
                + "tstamp timestamp NOT NULL,"
                + "link text NOT NULL,"
                + "type varchar(10) NOT NULL,"
                + "FOREIGN KEY(framework_id) REFERENCES frameworks,"
                + "FOREIGN KEY(user_id) REFERENCES users"
                + ")");

    }

    @Override
    public Content getById(long contentId) {
        final List<Content> toReturn = jdbcTemplate.query("SELECT * FROM content WHERE content_id = ?", ROW_MAPPER, contentId);
        if (toReturn.isEmpty()) {
            return null;
        }
        return toReturn.get(0);
    }

    @Override
    public List<Content> getContentByFramework(long frameworkId) {
        final List<Content> toReturn = jdbcTemplate.query("SELECT * FROM content where framework_id = ?", ROW_MAPPER, frameworkId);

        if (toReturn.isEmpty()) {
            return null;
        }

        return toReturn;
    }

    @Override
    public List<Content> getContentByFrameworkAndUser(long frameworkId, long userId) {
        final List<Content> toReturn = jdbcTemplate.query("SELECT * FROM content WHERE framework_id = ? AND user_id = ?", ROW_MAPPER, frameworkId, userId);

        if (toReturn.isEmpty()) {
            return null;
        }

        return toReturn;
    }

    @Override
    public List<Content> getContentByFrameworkAndType(long frameworkId, ContentTypes type) {
        final List<Content> toReturn = jdbcTemplate.query("SELECT * FROM content WHERE framework_id = ? AND type = ?", ROW_MAPPER, frameworkId, type.name());

        if (toReturn.isEmpty()) {
            return null;
        }

        return toReturn;
    }

    @Override
    public List<Content> getContentByUser(long userId) {
        final List<Content> toReturn = jdbcTemplate.query("SELECT * FROM comments WHERE user_id = ?", ROW_MAPPER, userId);

        if (toReturn.isEmpty()) {
            return null;
        }

        return toReturn;
    }

    @Override
    public Content insertContent(long frameworkId, long userId, String title, URL url, ContentTypes type) {
        long millis = System.currentTimeMillis();

        final Map<String, Object> args = new HashMap<>();
        args.put("framework_id", frameworkId);
        args.put("user_id",userId);
        args.put("title", title);
        args.put("votes_up", 0);
        args.put("votes_down", 0);
        args.put("tstamp", millis);
        args.put("link", url.toString());
        args.put("type", type.name());

        final Number voteId = jdbcInsert.executeAndReturnKey(args);
        return new Content (voteId.longValue(), frameworkId, userId, title, 0, 0, new Timestamp(millis), url, type);
    }

    @Override
    public int deleteContent(long contentId) {
        return jdbcTemplate.update("DELETE FROM content WHERE content_id = ?", contentId);
    }

    @Override
    public Content changeContent(long contentId, String title, URL url, ContentTypes type) {
        jdbcTemplate.update("UPDATE content SET title = ?, link = ?, type = ? WHERE content_id = ?", title, url.toString(), type.name());
        return getById(contentId);
    }

}
