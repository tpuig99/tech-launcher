package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Comment;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.VerificationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

@Repository
public class VerificationTokenDaoImpl implements VerificationTokenDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final static RowMapper<VerificationToken> ROW_MAPPER = VerificationTokenDaoImpl::mapRow;
    @Autowired
    public VerificationTokenDaoImpl(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("verification_token")
                .usingGeneratedKeyColumns("token_id");
    }

    private static VerificationToken mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new VerificationToken(rs.getInt("token_id"),
                rs.getString("token"),
                rs.getInt("user_id"),
                rs.getTimestamp("exp_date"));
    }

    @Override
    public void insert(long userId, String token) {
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(ts);
        calendar.add(Calendar.MINUTE,60*24);
        ts = new Timestamp(calendar.getTime().getTime());
        final Map<String, Object> args = new HashMap<>();
        args.put("token", token);
        args.put("user_id", userId);
        args.put("exp_date", ts);

        Number token_id = jdbcInsert.executeAndReturnKey(args);
    }

    @Override
    public Optional<VerificationToken> getById(long tokenId) {
        return jdbcTemplate.query("SELECT * FROM verification_token WHERE token_id = ?", new Object[] {tokenId}, ROW_MAPPER).stream().findFirst();
    }

    @Override
    public Optional<VerificationToken> getByUser(long userId) {
        return jdbcTemplate.query("SELECT * FROM verification_token WHERE user_id = ?", new Object[] { userId }, ROW_MAPPER).stream().findFirst();
    }

    @Override
    public int deleteById(long tokenId) {
        return jdbcTemplate.update("DELETE FROM verification_token WHERE token_id = ?", tokenId);
    }

    @Override
    public int deleteByUser(long userId) {
        return jdbcTemplate.update("DELETE FROM verification_token WHERE user_id = ?",userId);
    }

    @Override
    public void change(long tokenId, String token) {
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(ts);
        calendar.add(Calendar.MINUTE,60*24);
        ts = new Timestamp(calendar.getTime().getTime());
        String sql = "UPDATE verification_token set token=?,exp_date=? where token_id=?";
        jdbcTemplate.update(sql,token,ts,tokenId);
    }

    @Override
    public Optional<VerificationToken> getByToken(String token) {
        return jdbcTemplate.query("SELECT * FROM verification_token WHERE token = ?", new Object[] {token}, ROW_MAPPER).stream().findFirst();
    }
}
