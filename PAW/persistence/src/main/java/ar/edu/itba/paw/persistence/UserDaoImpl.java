package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Comment;
import ar.edu.itba.paw.models.User;
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
                rs.getString("user_description"));
    }

    private static String usernameMapRow(ResultSet rs, int rowNum) throws SQLException {
        return rs.getString("user_name");
    }

    private static String mailMapRow(ResultSet rs, int rowNum) throws SQLException {
        return rs.getString("mail");
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query("SELECT * FROM users", ROW_MAPPER);
    }

    @Override
    public Optional<User> findById(final long id) {
        return jdbcTemplate.query("SELECT * FROM users WHERE user_id = ?", new Object[] { id }, ROW_MAPPER).stream().findFirst();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return jdbcTemplate.query("SELECT * FROM users WHERE user_name ILIKE ?", new Object[] {username}, ROW_MAPPER).stream().findFirst();
    }

    @Override
    public Optional<User> findByUsernameOrMail(String username, String mail) {
        return jdbcTemplate.query("SELECT * FROM users WHERE user_name ILIKE ? or mail =?", new Object[] {username, mail}, ROW_MAPPER).stream().findFirst();
    }

    @Override
    public Optional<User> findByMail(String mail) {
        return jdbcTemplate.query("SELECT * FROM users WHERE mail = ?", new Object[] { mail }, ROW_MAPPER).stream().findFirst();
    }

    @Override
    public User create(String user_name,String mail) {
        final Map<String, Object> args = new HashMap<>();
        args.put("user_name", user_name); // la key es el nombre de la columna
        args.put("mail",mail);
        args.put("enabled",false);
        args.put("user_description","");
        final Number userId = jdbcInsert.executeAndReturnKey(args);
        return new User(userId.longValue(), user_name,mail);
    }
    @Override
    public User create(String user_name,String mail,String password) {
        final Map<String, Object> args = new HashMap<>();
        args.put("user_name", user_name); // la key es el nombre de la columna
        args.put("mail",mail);
        args.put("password",password);
        args.put("user_description","");
        args.put("enabled",false);
        final Number userId = jdbcInsert.executeAndReturnKey(args);
        return new User(userId.longValue(), user_name,mail,password);
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

    // TODO: CAN IT BE REPLACED WITH A NATURAL JOIN? QUERY IS A LITTLE BIT TRICKY
    @Override
    public Map<Long, String> getUsernamesByComments(List<Comment> comments) {

        final Map<Long, String> toReturn = new HashMap<>();
        String username;
        if(comments !=null ) {
            for (Comment c : comments) {
                Optional<User> user = findById(c.getUserId());
                if (user.isPresent()) {
                    username = user.get().getUsername();
                    toReturn.put(c.getCommentId(), username);
                }
            }
        }

        return toReturn;
    }

    @Override
    public List<String> getMails() {
        return jdbcTemplate.query("SELECT mail FROM users", ROW_MAPPER_MAIL);
    }

    // TODO: IS THIS ALRIGHT? ROW_MAPPER_MAIL???
    @Override
    public List<String> getUserNames() {
        return jdbcTemplate.query("SELECT user_name FROM users", ROW_MAPPER_MAIL);
    }

    @Override
    public void setEnable(long id) {
        jdbcTemplate.update("UPDATE users set enabled=true where user_id=?",id);
    }

    @Override
    public void updateDescription(long id, String description) {
        jdbcTemplate.update("UPDATE users set user_description=? where user_id=?",description,id);
    }
}