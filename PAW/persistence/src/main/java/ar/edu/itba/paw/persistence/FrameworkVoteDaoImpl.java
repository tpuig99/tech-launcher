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

@Repository
public class FrameworkVoteDaoImpl implements FrameworkVoteDao {
    private JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final static RowMapper<FrameworkVote> ROW_MAPPER = new
            RowMapper<FrameworkVote>() {
                @Override
                public FrameworkVote mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return new FrameworkVote(rs.getInt("vote_id"),rs.getInt("framework_id"),rs.getInt("user_id"),rs.getInt("stars"));
                }
            };
    private final static RowMapper<FrameworkVote> ROW_MAPPER_WITH_FRAMEWORK_NAME = new
            RowMapper<FrameworkVote>() {
                @Override
                public FrameworkVote mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return new FrameworkVote(rs.getInt("vote_id"),rs.getInt("framework_id"),rs.getInt("user_id"),rs.getInt("stars"),rs.getString("framework_name"));
                }
            };

    @Autowired
    public FrameworkVoteDaoImpl(final DataSource ds) {
        this.jdbcTemplate = new JdbcTemplate(ds);
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("framework_votes")
                .usingGeneratedKeyColumns("vote_id");
    }

    @Override
    public List<FrameworkVote> getAll() {
        final List<FrameworkVote> toReturn = jdbcTemplate.query("SELECT * FROM framework_votes", ROW_MAPPER);
        return toReturn;
    }

    @Override
    public List<FrameworkVote> getByFramework(long frameworkId) {
        final List<FrameworkVote> toReturn = jdbcTemplate.query("SELECT * FROM framework_votes WHERE framework_id=?", ROW_MAPPER,frameworkId);
        return toReturn;
    }

    @Override
    public FrameworkVote getById(long voteId) {
        final List<FrameworkVote> toReturn = jdbcTemplate.query("SELECT * FROM framework_votes WHERE vote_id=?", ROW_MAPPER,voteId);
        if (toReturn.isEmpty()) {
            return null;
        }
        return toReturn.get(0);
    }

    @Override
    public FrameworkVote getByFrameworkAndUser(long frameworkId, long userId) {
        final List<FrameworkVote> toReturn = jdbcTemplate.query("SELECT * FROM framework_votes WHERE framework_id=? AND user_id=?", ROW_MAPPER,frameworkId,userId);
        if (toReturn.isEmpty()) {
            return null;
        }
        return toReturn.get(0);
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
        String sql = "DELETE FROM framework_votes where vote_id=?";
        return jdbcTemplate.update(sql,voteId);
    }

    @Override
    public FrameworkVote update(long voteId, int stars) {
        String sql = "UPDATE framework_votes set stars=? where vote_id=?";
        jdbcTemplate.update(sql,stars,voteId);
        return getById(voteId);
    }

    @Override
    public List<FrameworkVote> getAllByUserWithFrameworkName(long userId) {
        return jdbcTemplate.query("select framework_id, vote_id,user_id,stars,framework_name from framework_votes natural join frameworks where user_id =?", new Object[] { userId }, ROW_MAPPER_WITH_FRAMEWORK_NAME);
    }
}
