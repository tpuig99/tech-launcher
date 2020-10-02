package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.ContentVote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class ContentVoteDaoImpl implements ContentVoteDao{
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final static RowMapper<ContentVote> ROW_MAPPER = ContentVoteDaoImpl::mapRow;

    @Autowired
    public ContentVoteDaoImpl(final DataSource ds) {
        this.jdbcTemplate = new JdbcTemplate(ds);
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("content_votes")
                .usingGeneratedKeyColumns("vote_id");
    }

    private static ContentVote mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new ContentVote(rs.getInt("vote_id"),
                rs.getInt("content_id"),
                rs.getInt("user_id"),
                rs.getInt("vote"));
    }


    @Override
    public Optional<ContentVote> getById(long voteId) {
        return jdbcTemplate.query("SELECT * FROM content_votes WHERE vote_id=?", new Object[] {voteId}, ROW_MAPPER).stream().findFirst();
    }

    @Override
    public Optional<ContentVote> getByContentAndUser(long contentId, long userId) {
        return jdbcTemplate.query("SELECT * FROM content_votes WHERE content_id=? AND user_id=?", new Object[] {contentId,userId}, ROW_MAPPER).stream().findFirst();
    }

    @Override
    public ContentVote insert(long contentId, long userId, int vote) {
        final Map<String, Object> args = new HashMap<>();
        args.put("content_id", contentId); // la key es el nombre de la columna
        args.put("user_id",userId);
        args.put("vote",vote);
        final Number voteId = jdbcInsert.executeAndReturnKey(args);
        return new ContentVote(voteId.longValue(), contentId,userId,vote);
    }

    @Override
    public int delete(long voteId) {
        return jdbcTemplate.update("DELETE FROM content_votes where vote_id=?",voteId);
    }

    @Override
    public void update(long voteId, int vote) {
        jdbcTemplate.update("UPDATE content_votes set vote=? where vote_id=?",vote,voteId);
    }
}
