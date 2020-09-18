package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Content;
import ar.edu.itba.paw.models.ContentTypes;
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
                            rs.getString("link"),
                            Enum.valueOf(ContentTypes.class, rs.getString("type")),
                            rs.getBoolean("pending")
                    );
                }
            };
    private final static RowMapper<Content> ROW_MAPPER_PROFILE = new
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
                            rs.getString("link"),
                            Enum.valueOf(ContentTypes.class, rs.getString("type")),
                            rs.getBoolean("pending"),
                            rs.getString("framework_name")
                    );
                }
            };

    @Autowired
    public ContentDaoImpl(final DataSource ds) {
        this.jdbcTemplate = new JdbcTemplate(ds);

        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("content")
                .usingGeneratedKeyColumns("content_id");

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
        return toReturn;
    }

    @Override
    public List<Content> getContentByFrameworkAndUser(long frameworkId, long userId) {
        final List<Content> toReturn = jdbcTemplate.query("SELECT * FROM content WHERE framework_id = ? AND user_id = ?", ROW_MAPPER, frameworkId, userId);

        return toReturn;
    }

    @Override
    public List<Content> getContentByFrameworkAndType(long frameworkId, ContentTypes type) {
        final List<Content> toReturn = jdbcTemplate.query("SELECT * FROM content WHERE framework_id = ? AND type = ?", ROW_MAPPER, frameworkId, type.name());
        return toReturn;
    }

    @Override
    public List<Content> getContentByUser(long userId) {
        final List<Content> toReturn = jdbcTemplate.query("select content_id,title, frameworks.framework_id,user_id,votes_up,votes_down,tstamp,link,content.type,pending,framework_name from content join frameworks on content.framework_id = frameworks.framework_id where user_id=?", ROW_MAPPER_PROFILE, userId);
        return toReturn;
    }

    @Override
    public Content insertContent(long frameworkId, long userId, String title, String link, ContentTypes type, Boolean pending) {
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        final Map<String, Object> args = new HashMap<>();
        args.put("framework_id", frameworkId);
        args.put("user_id",userId);
        args.put("title", title);
        args.put("votes_up", 0);
        args.put("votes_down", 0);
        args.put("tstamp", ts);
        args.put("link", link);
        args.put("type", type.name());
        args.put("pending", pending);

        final Number voteId = jdbcInsert.executeAndReturnKey(args);
        return new Content (voteId.longValue(), frameworkId, userId, title, 0, 0, ts, link, type, pending);
    }

    @Override
    public int deleteContent(long contentId) {
        return jdbcTemplate.update("DELETE FROM content WHERE content_id = ?", contentId);
    }

    @Override
    public Content changeContent(long contentId, String title, String link, ContentTypes type) {
        jdbcTemplate.update("UPDATE content SET title = ?, link = ?, type = ? WHERE content_id = ?", title, link, type.name());
        return getById(contentId);
    }

}
