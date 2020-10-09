package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class VerifyUserDaoImpl implements VerifyUserDao {
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedJdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final static RowMapper<VerifyUser> ROW_MAPPER= VerifyUserDaoImpl::VerifyMapRow;
    private final static String SELECTION_VERIFY="select v.verification_id, v.framework_id,v.user_id,v.comment_id,v.pending,user_name,framework_name,c.description,c.tstamp,(CASE WHEN v.comment_id IS NULL THEN false ELSE true END) AS has_comment, count(case when vote=-1 then vote end) as neg, reference, count(case when vote=1 then vote end) as pos,(CASE WHEN v.pending IS false THEN true ELSE false END) AS is_verify,f.category,CASE WHEN admin_id IS NULL THEN false ELSE true END AS is_admin from verify_users v left join frameworks f on f.framework_id = v.framework_id left join comments c on c.comment_id = v.comment_id left join users u on u.user_id = v.user_id left join comment_votes cv on v.comment_id = cv.comment_id left join admins a on c.user_id = a.user_id ";
    private final static String GROUP_BY=" group by v.verification_id,u.user_name,f.framework_name,c.description, v.framework_id, v.user_id, v.comment_id, v.pending, user_name, framework_name, v.verification_id, c.tstamp, (CASE WHEN v.comment_id IS NULL THEN false ELSE true END),c.reference,f.category,a.admin_id";
    @Autowired
    CommentDao cd;
    @Autowired
    FrameworkDao fd;
    @Autowired
    UserDao ud;

    @Autowired
    public VerifyUserDaoImpl(final DataSource ds) {
        namedJdbcTemplate = new NamedParameterJdbcTemplate(ds);
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                    .withTableName("verify_users")
                    .usingGeneratedKeyColumns("verification_id");
    }
    private static VerifyUser VerifyMapRow(ResultSet rs, int i) throws SQLException {
        if(rs.getBoolean("has_comment")){
            Comment comment = new Comment(rs.getLong("comment_id"),
                    rs.getInt("framework_id"),
                    rs.getLong("user_id"),
                    rs.getString("description"),
                    rs.getInt("pos"),
                    rs.getInt("neg"),
                    rs.getTimestamp("tstamp"),
                    rs.getLong("reference"),
                    rs.getString("framework_name"),
                    rs.getString("user_name"),
                    FrameworkCategories.getByName(rs.getString("category")),
                    rs.getBoolean("is_verify"),
                    rs.getBoolean("is_admin")
            );
            return new VerifyUser(rs.getInt("verification_id"),comment,rs.getBoolean("pending"));
        }
        return new VerifyUser(rs.getInt("verification_id"),
                rs.getLong("user_id"),
                rs.getString("user_name"),
                rs.getInt("framework_id"),
                rs.getString("framework_name"),
                rs.getBoolean("pending"),
                FrameworkCategories.getByName(rs.getString("category"))
        );
    }


    @Override
    public VerifyUser create(long userId, long frameworkId, long commentId) {
        final Map<String, Object> args = new HashMap<>();
        args.put("user_id", userId); // la key es el nombre de la columna
        args.put("framework_id",frameworkId);
        args.put("comment_id",commentId);
        args.put("pending",true);
        final Number verify = jdbcInsert.executeAndReturnKey(args);
        Optional<Comment> comment = cd.getById(commentId);
        return new VerifyUser(verify.longValue(),comment.get(),false);
    }

    @Override
    public VerifyUser create(long userId, long frameworkId) {
        final Map<String, Object> args = new HashMap<>();
        args.put("user_id", userId); // la key es el nombre de la columna
        args.put("framework_id",frameworkId);
        args.put("comment_id",null);
        args.put("pending",true);
        final Number verify = jdbcInsert.executeAndReturnKey(args);
        Optional<Framework> frm = fd.findById(frameworkId);
        Optional<User> user = ud.findById(userId);
        return new VerifyUser(verify.longValue(),userId,user.get().getUsername(),frameworkId,frm.get().getName(),false);
    }

    @Override
    public List<VerifyUser> getByUser(long userId, boolean pending) {
        String value = SELECTION_VERIFY+"WHERE v.user_id = ? and v.pending = ?"+GROUP_BY;
        return jdbcTemplate.query(value, new Object[] {userId,pending}, ROW_MAPPER);
    }

    @Override
    public List<VerifyUser> getByFramework(long frameworkId, boolean pending) {
        if( !pending ) {
            String value = SELECTION_VERIFY + "WHERE v.framework_id = ? and v.pending = ?" + GROUP_BY;
            return jdbcTemplate.query(value, new Object[]{frameworkId, pending}, ROW_MAPPER);
        }
        String value = SELECTION_VERIFY + "WHERE v.framework_id = ? and v.pending = ? and v.comment_id IS NOT NULL" + GROUP_BY;
        return jdbcTemplate.query(value, new Object[]{frameworkId, pending}, ROW_MAPPER);
    }

    @Override
    public List<VerifyUser> getByFrameworks( List<Long> frameworksIds, boolean pending){
        Map<String, List<Long>> params = new HashMap<>();
        params.put("framework_id", frameworksIds);
        String query;

        if( !pending )
            query = SELECTION_VERIFY + " WHERE v.framework_id IN (:framework_id) and v.pending = false" + GROUP_BY;
        else
            query = SELECTION_VERIFY + " WHERE v.framework_id IN (:framework_id) and v.pending = true and v.comment_id IS NOT NULL " + GROUP_BY;

        return namedJdbcTemplate.query(query, params, ROW_MAPPER);
    }

    @Override
    public Optional<VerifyUser> getByFrameworkAndUser(long frameworkId, long userId) {
        String value = SELECTION_VERIFY+"WHERE v.framework_id = ? and v.user_id = ?"+GROUP_BY;
        return jdbcTemplate.query(value, new Object[] {frameworkId,userId}, ROW_MAPPER).stream().findFirst();
    }

    @Override
    public List<VerifyUser> getAllByUser(long userId) {
        String value = SELECTION_VERIFY+"WHERE v.user_id = ?"+GROUP_BY;
        return jdbcTemplate.query(value, new Object[] {userId}, ROW_MAPPER);        }

    @Override
    public List<VerifyUser> getAllByFramework(long frameworkId) {
        String value = SELECTION_VERIFY+"WHERE v.framework_id = ?"+GROUP_BY;
        return jdbcTemplate.query(value, new Object[] {frameworkId}, ROW_MAPPER);
    }

    @Override
    public Optional<VerifyUser> getById(long verificationId) {
        String value = SELECTION_VERIFY+"WHERE v.verification_id = ?"+GROUP_BY;
        return jdbcTemplate.query(value, new Object[] {verificationId}, ROW_MAPPER).stream().findFirst();
    }

    @Override
    public List<VerifyUser> getByPending(boolean pending, long page, long pageSize) {
        if( !pending ) {
            String value = SELECTION_VERIFY + "WHERE v.pending = ?" + GROUP_BY + " LIMIT ? OFFSET ?";
            return jdbcTemplate.query(value, new Object[]{pending, pageSize, (page - 1) * pageSize}, ROW_MAPPER);
        }
        String value = SELECTION_VERIFY + "WHERE v.pending = ? AND v.comment_id IS NOT NULL" + GROUP_BY + " LIMIT ? OFFSET ?";
        return jdbcTemplate.query(value, new Object[]{pending, pageSize, (page - 1) * pageSize}, ROW_MAPPER);
    }

    @Override
    public void delete(long verificationId) {
        jdbcTemplate.update("DELETE FROM verify_users WHERE verification_id = ?", new Object[]{verificationId});
    }

    @Override
    public void deleteVerificationByUser(long userId) {
        jdbcTemplate.update("DELETE FROM verify_users WHERE user_id = ?", new Object[]{userId});
    }

    @Override
    public void verify(long verificationId) {
        jdbcTemplate.update("UPDATE verify_users SET pending = false WHERE verification_id = ?", new Object[]{verificationId});
    }

    @Override
    public List<VerifyUser> getApplicantsByPending(boolean pending, long page, long pageSize) {
        String value = SELECTION_VERIFY+"WHERE v.pending = ? AND v.comment_id IS NULL"+GROUP_BY + " LIMIT ? OFFSET ?";
        return jdbcTemplate.query(value, new Object[] {pending, pageSize, (page-1)*pageSize}, ROW_MAPPER);
    }

    @Override
    public List<VerifyUser> getApplicantsByFrameworks(List<Long> frameworksIds) {
        Map<String, List<Long>> params = new HashMap<>();
        params.put("framework_id", new ArrayList<>(frameworksIds));
        String query;
        query = SELECTION_VERIFY + " WHERE v.framework_id IN (:framework_id) and v.pending = true and v.comment_id IS NULL " + GROUP_BY;

        return namedJdbcTemplate.query(query, params, ROW_MAPPER);
    }
}
