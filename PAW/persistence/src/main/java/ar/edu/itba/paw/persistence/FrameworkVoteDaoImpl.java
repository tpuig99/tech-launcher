package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.FrameworkVote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class FrameworkVoteDaoImpl implements FrameworkVoteDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final static RowMapper<FrameworkVote> ROW_MAPPER = FrameworkVoteDaoImpl::frameworkMapRow;
    private final static RowMapper<FrameworkVote> ROW_MAPPER_WITH_FRAMEWORK_NAME = FrameworkVoteDaoImpl::frameworkNameMapRow;

    @Autowired
    public FrameworkVoteDaoImpl(final DataSource ds) {
        this.jdbcTemplate = new JdbcTemplate(ds);
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("framework_votes")
                .usingGeneratedKeyColumns("vote_id");
    }

    private static FrameworkVote frameworkMapRow(ResultSet rs, int rowNum) throws SQLException {
        return new FrameworkVote(rs.getInt("vote_id"),
                rs.getInt("framework_id"),
                rs.getInt("user_id"),
                rs.getInt("stars"));
    }

    private static FrameworkVote frameworkNameMapRow(ResultSet rs, int rowNum) throws SQLException {
        return new FrameworkVote(rs.getInt("vote_id"),
                rs.getInt("framework_id"),
                rs.getInt("user_id"),
                rs.getInt("stars"),
                rs.getString("framework_name"));
    }

    @Override
    public List<FrameworkVote> getAll() {
        return jdbcTemplate.query("SELECT * FROM framework_votes", ROW_MAPPER);
    }

    @Override
    public List<FrameworkVote> getByFramework(long frameworkId) {
        return jdbcTemplate.query("SELECT * FROM framework_votes WHERE framework_id=?", ROW_MAPPER,frameworkId);
    }

    @Override
    public Optional<FrameworkVote> getById(long voteId) {
        return jdbcTemplate.query("SELECT * FROM framework_votes WHERE vote_id=?", new Object[] {voteId}, ROW_MAPPER).stream().findFirst();
    }

    @Override
    public Optional<FrameworkVote> getByFrameworkAndUser(long frameworkId, long userId) {
        return jdbcTemplate.query("SELECT * FROM framework_votes WHERE framework_id=? AND user_id=?", new Object[] {frameworkId,userId}, ROW_MAPPER).stream().findFirst();
    }

    @Override
    public List<FrameworkVote> getAllByUser(long userId) {
        return jdbcTemplate.query("SELECT * FROM framework_votes WHERE user_id=?", new Object[] { userId }, ROW_MAPPER);
    }

    @Override
    public FrameworkVote insert(long frameworkId, long userId, int stars) {
        final Map<String, Object> args = new HashMap<>();
        args.put("framework_id", frameworkId); // la key es el nombre de la columna
        args.put("user_id",userId);
        args.put("stars",stars);
        final Number voteId = jdbcInsert.executeAndReturnKey(args);
        return new FrameworkVote(voteId.longValue(), frameworkId,userId,stars);
    }

    @Override
    public int delete(long voteId) {
        return jdbcTemplate.update("DELETE FROM framework_votes where vote_id=?",voteId);
    }

    @Override
    public Optional<FrameworkVote> update(long voteId, int stars) {
        jdbcTemplate.update("UPDATE framework_votes set stars=? where vote_id=?",stars,voteId);
        return getById(voteId);
    }

    @Override
    public List<FrameworkVote> getAllByUserWithFrameworkName(long userId) {
        return jdbcTemplate.query("select framework_id, vote_id,user_id,stars,framework_name from framework_votes natural join frameworks where user_id =?", new Object[] { userId }, ROW_MAPPER_WITH_FRAMEWORK_NAME);
    }
}
