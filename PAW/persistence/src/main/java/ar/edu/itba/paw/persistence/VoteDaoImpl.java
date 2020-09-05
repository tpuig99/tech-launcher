package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Framework;
import ar.edu.itba.paw.models.FrameworkCategories;
import ar.edu.itba.paw.models.Vote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class VoteDaoImpl implements VoteDao{
    private JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final static RowMapper<Vote> ROW_MAPPER = new
            RowMapper<Vote>() {
                @Override
                public Vote mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return new Vote(rs.getInt("voteid"),rs.getInt("frameworkid"),rs.getInt("userid"),rs.getInt("stars"));
                }
            };

    @Autowired
    public VoteDaoImpl(final DataSource ds) {
        this.jdbcTemplate = new JdbcTemplate(ds);
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("votes")
                .usingGeneratedKeyColumns("voteid");

        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS votes ("
                + "voteid SERIAL PRIMARY KEY,"
                + "userid integer,"
                + "frameworkid integer,"
                + "stars integer"
                + ")");
    }

    @Override
    public List<Vote> getVotes() {
        return null;
    }

    @Override
    public Vote getVote(int frameworkId, int userId) {
        return null;
    }

    @Override
    public Vote insertVote(int frameworkId, int userId, int stars) {
        return null;
    }

    @Override
    public Vote deleteVote(int voteId) {
        return null;
    }

    @Override
    public Vote changeVote(int voteId, int stars) {
        return null;
    }
}
