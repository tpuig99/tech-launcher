package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Framework;
import ar.edu.itba.paw.models.FrameworkCategories;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.Vote;
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
public class VoteDaoImpl implements VoteDao{
    private JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final static RowMapper<Vote> ROW_MAPPER = new
            RowMapper<Vote>() {
                @Override
                public Vote mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return new Vote(rs.getInt("vote_id"),rs.getInt("framework_id"),rs.getInt("user_id"),rs.getInt("stars"));
                }
            };

    @Autowired
    public VoteDaoImpl(final DataSource ds) {
        this.jdbcTemplate = new JdbcTemplate(ds);
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("votes")
                .usingGeneratedKeyColumns("vote_id");

        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS votes ("
                + "vote_id SERIAL PRIMARY KEY,"
                + "user_id integer NOT NULL,"
                + "framework_id integer NOT NULL,"
                + "stars integer NOT NULL"
                + ")");
    }

    @Override
    public List<Vote> getAll() {
        final List<Vote> toReturn = jdbcTemplate.query("SELECT * FROM votes", ROW_MAPPER);
        if (toReturn.isEmpty()) {
            return null;
        }
        return toReturn;
    }

    @Override
    public List<Vote> getByFramework(long frameworkId) {
        final List<Vote> toReturn = jdbcTemplate.query("SELECT * FROM votes WHERE framework_id=?", ROW_MAPPER,frameworkId);
        if (toReturn.isEmpty()) {
            return null;
        }
        return toReturn;
    }

    @Override
    public Vote getById(long voteId) {
        final List<Vote> toReturn = jdbcTemplate.query("SELECT * FROM votes WHERE vote_id=?", ROW_MAPPER,voteId);
        if (toReturn.isEmpty()) {
            return null;
        }
        return toReturn.get(0);
    }

    @Override
    public Vote getByFrameworkAndUser(long frameworkId, long userId) {
        final List<Vote> toReturn = jdbcTemplate.query("SELECT * FROM votes WHERE framework_id=? AND user_id=?", ROW_MAPPER,frameworkId,userId);
        if (toReturn.isEmpty()) {
            return null;
        }
        return toReturn.get(0);
    }

    @Override
    public Vote insert(long frameworkId, long userId, int stars) {
        final Map<String, Object> args = new HashMap<>();
        args.put("framework_id", frameworkId); // la key es el nombre de la columna
        args.put("user_id",userId);
        args.put("stars",stars);
        final Number voteId = jdbcInsert.executeAndReturnKey(args);
        return new Vote(voteId.longValue(), frameworkId,userId,stars);
    }

    @Override
    public int delete(long voteId) {
        String sql = "DELETE FROM votes where vote_id=?";
        return jdbcTemplate.update(sql,voteId);
    }

    @Override
    public Vote update(long voteId, int stars) {
        String sql = "UPDATE votes set stars=? where vote_id=?";
        jdbcTemplate.update(sql,stars,voteId);
        return getById(voteId);
    }
}
