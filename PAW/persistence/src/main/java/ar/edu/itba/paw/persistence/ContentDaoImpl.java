package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Content;
import ar.edu.itba.paw.models.ContentTypes;
import ar.edu.itba.paw.models.FrameworkCategories;
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
public class ContentDaoImpl implements ContentDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    private final static RowMapper<Content> ROW_MAPPER = ContentDaoImpl::mapRow;
    private final static RowMapper<Content> ROW_MAPPER_PROFILE = ContentDaoImpl::mapRow2;

    @Autowired
    public ContentDaoImpl(final DataSource ds) {
        this.jdbcTemplate = new JdbcTemplate(ds);

        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("content")
                .usingGeneratedKeyColumns("content_id");

    }

    private static Content mapRow(ResultSet rs, int rowNum) throws SQLException {
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

    private static Content mapRow2(ResultSet rs, int rowNum) throws SQLException {
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
                rs.getString("framework_name"),
                FrameworkCategories.getByName(rs.getString("category"))
        );
    }

    @Override
    public Optional<Content> getById(long contentId) {
        return jdbcTemplate.query("SELECT * FROM content WHERE content_id = ?", new Object[] {contentId}, ROW_MAPPER).stream().findFirst();
    }

    @Override
    public List<Content> getContentByFramework(long frameworkId) {
        return jdbcTemplate.query("SELECT * FROM content where framework_id = ?", new Object[] { frameworkId }, ROW_MAPPER);
    }

    @Override
    public List<Content> getContentByFrameworkAndUser(long frameworkId, long userId) {
        return jdbcTemplate.query("SELECT * FROM content WHERE framework_id = ? AND user_id = ?", new Object[] { frameworkId, userId }, ROW_MAPPER);
    }

    @Override
    public List<Content> getContentByFrameworkAndType(long frameworkId, ContentTypes type) {
        return jdbcTemplate.query("SELECT * FROM content WHERE framework_id = ? AND type = ?", new Object[] { frameworkId, type.name() }, ROW_MAPPER);
    }

    @Override
    public List<Content> getNotPendingContentByFrameworkAndType(long frameworkId, ContentTypes type) {
        return jdbcTemplate.query("SELECT * FROM content WHERE framework_id = ? AND type = ? AND pending = false", new Object[] { frameworkId, type.name() }, ROW_MAPPER);
    }

    @Override
    public List<Content> getPendingContentByFrameworkAndType(long frameworkId, ContentTypes type) {
        return jdbcTemplate.query("SELECT * FROM content WHERE framework_id = ? AND type = ? AND pending = true", new Object[] { frameworkId, type.name() }, ROW_MAPPER);
    }

    @Override
    public List<Content> getContentByUser(long userId) {
        return jdbcTemplate.query("select content_id,title, frameworks.framework_id,user_id,votes_up,votes_down,tstamp,link,content.type,pending,framework_name,category from content join frameworks on content.framework_id = frameworks.framework_id where user_id=?",
                new Object[] { userId }, ROW_MAPPER_PROFILE);
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
    public Optional<Content> changeContent(long contentId, String title, String link, ContentTypes type) {
        jdbcTemplate.update("UPDATE content SET title = ?, link = ?, type = ? WHERE content_id = ?", title, link, type.name());
        return getById(contentId);
    }

}
