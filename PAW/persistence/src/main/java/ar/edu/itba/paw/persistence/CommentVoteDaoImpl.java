package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.CommentVote;
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
public class CommentVoteDaoImpl implements CommentVoteDao{
    private JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final static RowMapper<CommentVote> ROW_MAPPER = new
            RowMapper<CommentVote>() {
                @Override
                public CommentVote mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return new CommentVote(rs.getInt("vote_id"),rs.getInt("comment_id"),rs.getInt("user_id"),rs.getInt("vote"));
                }
            };

    @Autowired
    public CommentVoteDaoImpl(final DataSource ds) {
        this.jdbcTemplate = new JdbcTemplate(ds);
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("comment_votes")
                .usingGeneratedKeyColumns("vote_id");
    }


    @Override
    public List<CommentVote> getAll() {
        final List<CommentVote> toReturn = jdbcTemplate.query("SELECT * FROM comment_votes", ROW_MAPPER);
        return toReturn;
    }

    @Override
    public List<CommentVote> getByComment(long commentId) {
        final List<CommentVote> toReturn = jdbcTemplate.query("SELECT * FROM comment_votes WHERE comment_id=?", ROW_MAPPER,commentId);
        return toReturn;
    }

    @Override
    public CommentVote getById(long voteId) {
        final List<CommentVote> toReturn = jdbcTemplate.query("SELECT * FROM comment_votes WHERE vote_id=?", ROW_MAPPER,voteId);
        if (toReturn.isEmpty()) {
            return null;
        }
        return toReturn.get(0);
    }

    @Override
    public CommentVote getByCommentAndUser(long commentId, long userId) {
        final List<CommentVote> toReturn = jdbcTemplate.query("SELECT * FROM comment_votes WHERE comment_id=? AND user_id=?", ROW_MAPPER,commentId,userId);
        if (toReturn.isEmpty()) {
            return null;
        }
        return toReturn.get(0);
    }

    @Override
    public CommentVote insert(long commentId, long userId, int vote) {
        final Map<String, Object> args = new HashMap<>();
        args.put("comment_id", commentId); // la key es el nombre de la columna
        args.put("user_id",userId);
        args.put("vote",vote);
        final Number voteId = jdbcInsert.executeAndReturnKey(args);
        return new CommentVote(voteId.longValue(), commentId,userId,vote);
    }

    @Override
    public int delete(long voteId) {
        String sql = "DELETE FROM comment_votes where vote_id=?";
        return jdbcTemplate.update(sql,voteId);
    }

    @Override
    public CommentVote update(long voteId, int vote) {
        String sql = "UPDATE comment_votes set vote=? where vote_id=?";
        jdbcTemplate.update(sql,vote,voteId);
        return getById(voteId);
    }
}
