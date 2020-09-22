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
import java.util.Optional;

@Repository
public class CommentVoteDaoImpl implements CommentVoteDao{
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final static RowMapper<CommentVote> ROW_MAPPER = CommentVoteDaoImpl::mapRow;

    @Autowired
    public CommentVoteDaoImpl(final DataSource ds) {
        this.jdbcTemplate = new JdbcTemplate(ds);
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("comment_votes")
                .usingGeneratedKeyColumns("vote_id");
    }

    private static CommentVote mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new CommentVote(rs.getInt("vote_id"),
                rs.getInt("comment_id"),
                rs.getInt("user_id"),
                rs.getInt("vote"));
    }


    @Override
    public List<CommentVote> getAll() {
        return jdbcTemplate.query("SELECT * FROM comment_votes", ROW_MAPPER);
    }

    @Override
    public List<CommentVote> getByComment(long commentId) {
        return jdbcTemplate.query("SELECT * FROM comment_votes WHERE comment_id=?", new Object[] {commentId}, ROW_MAPPER);
    }

    @Override
    public Optional<CommentVote> getById(long voteId) {
        return jdbcTemplate.query("SELECT * FROM comment_votes WHERE vote_id=?", new Object[] {voteId}, ROW_MAPPER).stream().findFirst();
    }

    @Override
    public Optional<CommentVote> getByCommentAndUser(long commentId, long userId) {
        return jdbcTemplate.query("SELECT * FROM comment_votes WHERE comment_id=? AND user_id=?", new Object[] {commentId,userId}, ROW_MAPPER).stream().findFirst();
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
        return jdbcTemplate.update("DELETE FROM comment_votes where vote_id=?",voteId);
    }

    @Override
    public Optional<CommentVote> update(long voteId, int vote) {
        jdbcTemplate.update("UPDATE comment_votes set vote=? where vote_id=?",vote,voteId);
        return getById(voteId);
    }
}
