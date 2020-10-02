package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Comment;
import ar.edu.itba.paw.models.FrameworkCategories;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.VerifyUser;
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
public class UserDaoImpl implements UserDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    
    private final static RowMapper<User> ROW_MAPPER = UserDaoImpl::userMapRow;
    private final static RowMapper<String> ROW_MAPPER_USERNAME = UserDaoImpl::usernameMapRow;
    private final static RowMapper<String> ROW_MAPPER_MAIL = UserDaoImpl::mailMapRow;
    private final static RowMapper<VerifyUser> ROW_MAPPER_VERIFY_USER = UserDaoImpl::VerifyMapRow;


    private final static String SELECTION="select users.user_id,user_name,mail,password,enabled,user_description,allow_moderator,CASE WHEN admin_id IS NULL THEN false ELSE true END AS is_admin, picture from users left join admins a on users.user_id = a.user_id ";
    private final static String SELECTION_VERIFY="select v.verification_id, v.framework_id,v.user_id,v.comment_id,v.pending,user_name,framework_name,c.description,c.tstamp,(CASE WHEN v.comment_id IS NULL THEN false ELSE true END) AS has_comment, count(case when vote=-1 then vote end) as neg, reference, count(case when vote=1 then vote end) as pos,(CASE WHEN v.pending IS false THEN true ELSE false END) AS is_verify,f.category,CASE WHEN admin_id IS NULL THEN false ELSE true END AS is_admin from verify_users v left join frameworks f on f.framework_id = v.framework_id left join comments c on c.comment_id = v.comment_id left join users u on u.user_id = v.user_id left join comment_votes cv on v.comment_id = cv.comment_id left join admins a on c.user_id = a.user_id WHERE v.user_id=? group by v.verification_id,u.user_name,f.framework_name,c.description, v.framework_id, v.user_id, v.comment_id, v.pending, user_name, framework_name, v.verification_id, c.tstamp, (CASE WHEN v.comment_id IS NULL THEN false ELSE true END),c.reference,f.category,a.admin_id";
    @Autowired
    public UserDaoImpl(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                        .withTableName("users")
                        .usingGeneratedKeyColumns("user_id");
    }

    private static User userMapRow(ResultSet rs, int rowNum) throws SQLException {
        return new User(rs.getInt("user_id"),
                rs.getString("user_name"),
                rs.getString("mail"),
                rs.getString("password"),
                rs.getBoolean("enabled"),
                rs.getString("user_description"),
                rs.getBoolean("allow_moderator"),
                rs.getBoolean("is_admin"),
                rs.getBytes("picture"));
    }

    private static String usernameMapRow(ResultSet rs, int rowNum) throws SQLException {
        return rs.getString("user_name");
    }

    private static String mailMapRow(ResultSet rs, int rowNum) throws SQLException {
        return rs.getString("mail");
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
                rs.getBoolean("pending")
                );
    }


    @Override
    public List<User> getAll() {
        List<User> users = jdbcTemplate.query(SELECTION, ROW_MAPPER);
        getVerify(users);
        return users;
    }

    private void getVerify(List<User> users) {
        for (User user: users) {
            user.setVerifications(jdbcTemplate.query(SELECTION_VERIFY, new Object[] { user.getId()}, ROW_MAPPER_VERIFY_USER));
        }
    }
    private void getVerify(User user) {
            user.setVerifications(jdbcTemplate.query(SELECTION_VERIFY, new Object[] { user.getId()}, ROW_MAPPER_VERIFY_USER));

    }

    @Override
    public Optional<User> findById(final long id) {
        String value = SELECTION+"WHERE users.user_id = ?";
        Optional<User> user = jdbcTemplate.query(value, new Object[] { id }, ROW_MAPPER).stream().findFirst();
        if(user.isPresent())
            getVerify(user.get());
        return user;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        String value = SELECTION+"WHERE users.user_name ILIKE ?";
        Optional<User> user =  jdbcTemplate.query(value, new Object[] {username}, ROW_MAPPER).stream().findFirst();
        if(user.isPresent())
            getVerify(user.get());
        return user;
    }

    @Override
    public Optional<User> findByUsernameOrMail(String username, String mail) {
        String value = SELECTION+"WHERE users.user_name ILIKE ? or mail = ?";
        Optional<User> user = jdbcTemplate.query(value, new Object[] {username, mail}, ROW_MAPPER).stream().findFirst();
        if(user.isPresent())
            getVerify(user.get());
        return user;
    }

    @Override
    public Optional<User> findByMail(String mail) {
        String value = SELECTION+"WHERE users.mail = ?";
        Optional<User> user = jdbcTemplate.query(value, new Object[] { mail }, ROW_MAPPER).stream().findFirst();
        if(user.isPresent())
            getVerify(user.get());
        return user;
    }

    @Override
    public User create(String user_name,String mail,String password) {
        final Map<String, Object> args = new HashMap<>();
        args.put("user_name", user_name); // la key es el nombre de la columna
        args.put("mail",mail);
        args.put("password",password);
        args.put("user_description","");
        args.put("enabled",false);
        args.put("allow_moderator",true);
        args.put("picture",new byte[]{});
        final Number userId = jdbcInsert.executeAndReturnKey(args);
        return new User(userId.longValue(), user_name,mail,password,false,"",true,false, new byte[]{});
    }

    @Override
    public int delete(long userId) {
        return jdbcTemplate.update("DELETE FROM users where user_id=?",userId);
    }

    @Override
    public Optional<User> update(long userId, String user_name, String mail, String password) {
        jdbcTemplate.update("UPDATE users set mail=?,user_name=?,password=? where user_id=?",
                mail,user_name,password,userId);
        return findById(userId);
    }

    @Override
    public List<String> getMails() {
        return jdbcTemplate.query("SELECT mail FROM users", ROW_MAPPER_MAIL);
    }

    @Override
    public List<String> getUserNames() {
        return jdbcTemplate.query("SELECT user_name FROM users", ROW_MAPPER_USERNAME);
    }

    @Override
    public void setEnable(long id) {
        jdbcTemplate.update("UPDATE users set enabled=true where user_id=?",id);
    }

    @Override
    public void updateDescription(long id, String description) {
        jdbcTemplate.update("UPDATE users set user_description=? where user_id=?",description,id);
    }

    @Override
    public void updatePicture(long id, byte[] picture) {
        jdbcTemplate.update("UPDATE users set picture=? where user_id=?",picture,id);
    }

    @Override
    public void updatePassword(long id, String password) {
        jdbcTemplate.update("UPDATE users set password=? where user_id=?",password,id);
    }
}