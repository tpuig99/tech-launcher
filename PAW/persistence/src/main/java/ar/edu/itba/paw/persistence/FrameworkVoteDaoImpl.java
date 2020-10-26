package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.FrameworkCategories;
import ar.edu.itba.paw.models.FrameworkVote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import javax.swing.text.html.Option;
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
    private final static RowMapper<Integer> ROW_MAPPER_COUNT = FrameworkVoteDaoImpl::mapRowCount;
    private final static String SELECTION="select framework_name,v.framework_id,category,vote_id,user_id,stars from framework_votes v natural join frameworks ";

    @Autowired
    public FrameworkVoteDaoImpl(final DataSource ds) {
        this.jdbcTemplate = new JdbcTemplate(ds);
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("framework_votes")
                .usingGeneratedKeyColumns("vote_id");
    }

    private static Integer mapRowCount(ResultSet rs, int i) throws SQLException {
        return rs.getInt("count");
    }

    private static FrameworkVote frameworkMapRow(ResultSet rs, int rowNum) throws SQLException {
        return new FrameworkVote(rs.getInt("vote_id"),
                rs.getInt("framework_id"),
                rs.getInt("user_id"),
                rs.getInt("stars"),
                rs.getString("framework_name"),
                FrameworkCategories.getByName(rs.getString("category")));
    }


    @Override
    public List<FrameworkVote> getByFramework(long frameworkId) {
        String value=SELECTION+"WHERE v.framework_id=?";
        return jdbcTemplate.query(value, ROW_MAPPER,frameworkId);
    }

    @Override
    public Optional<FrameworkVote> getById(long voteId) {
        String value=SELECTION+"WHERE v.vote_id=?";
        return jdbcTemplate.query(value, new Object[] {voteId}, ROW_MAPPER).stream().findFirst();
    }

    @Override
    public Optional<FrameworkVote> getByFrameworkAndUser(long frameworkId, long userId) {
        String value=SELECTION+"WHERE v.framework_id=? AND user_id=?";
        return jdbcTemplate.query(value, new Object[] {frameworkId,userId}, ROW_MAPPER).stream().findFirst();
    }

    @Override
    public List<FrameworkVote> getAllByUser(long userId, long page, long pageSize) {
        String value=SELECTION+"WHERE user_id=? LIMIT ? OFFSET ?";
        return jdbcTemplate.query(value, new Object[] { userId, pageSize, pageSize * (page-1) }, ROW_MAPPER);
    }

    @Override
    public Optional<Integer> getAllCountByUser(long userId){
        return jdbcTemplate.query("select count (*) from framework_votes inner join users on framework_votes.user_id = users.user_id where framework_votes.user_id = ?", new Object[] {userId}, ROW_MAPPER_COUNT).stream().findFirst();
    }

    @Override
    public FrameworkVote insert(long frameworkId, long userId, int stars) {
        final Map<String, Object> args = new HashMap<>();
        args.put("framework_id", frameworkId); // la key es el nombre de la columna
        args.put("user_id",userId);
        args.put("stars",stars);
        final Number voteId = jdbcInsert.executeAndReturnKey(args);

        return getById(voteId.longValue()).get();
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

}
