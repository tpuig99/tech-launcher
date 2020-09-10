package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Comment;
import ar.edu.itba.paw.models.Framework;
import ar.edu.itba.paw.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserDaoImpl implements UserDao {
    private JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    
    private final static RowMapper<User> ROW_MAPPER = new
            RowMapper<User>() {
                @Override
                public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return new User(rs.getInt("user_id"), rs.getString("user_name"),rs.getString("mail"),rs.getString("password"));
                }
            };
    private final static RowMapper<String> ROW_MAPPER_USERNAME = new
            RowMapper<String>() {
                @Override
                public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return new String(rs.getString("user_name"));
                }
            };
    private final static RowMapper<String> ROW_MAPPER_MAIL = new
            RowMapper<String>() {
                @Override
                public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return new String(rs.getString("mail"));
                }
            };
    @Autowired
    public UserDaoImpl(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                        .withTableName("users")
                        .usingGeneratedKeyColumns("user_id");
    }

    @Override
    public List<User> getAll() {
        final List<User> toReturn = jdbcTemplate.query("SELECT * FROM users", ROW_MAPPER);

        return toReturn;
    }

    @Override
    public User findById(final long id) {
        final List<User> list = jdbcTemplate.query("SELECT * FROM users WHERE user_id = ?", ROW_MAPPER, id);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public User findByUsername(String username) {
        final List<User> list = jdbcTemplate.query("SELECT * FROM users WHERE user_name ILIKE ?", ROW_MAPPER, username);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public User findByMail(String mail) {
        final List<User> list = jdbcTemplate.query("SELECT * FROM users WHERE mail = ?", ROW_MAPPER, mail);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public User create(String user_name,String mail) {
        final Map<String, Object> args = new HashMap<>();
        args.put("user_name", user_name); // la key es el nombre de la columna
        args.put("mail",mail);
        final Number userId = jdbcInsert.executeAndReturnKey(args);
        return new User(userId.longValue(), user_name,mail);
    }
    @Override
    public User create(String user_name,String mail,String password) {
        final Map<String, Object> args = new HashMap<>();
        args.put("user_name", user_name); // la key es el nombre de la columna
        args.put("mail",mail);
        args.put("password",password);
        final Number userId = jdbcInsert.executeAndReturnKey(args);
        return new User(userId.longValue(), user_name,mail,password);
    }

    @Override
    public int delete(long userId) {
        String sql = "DELETE FROM usersdao where user_id=?";
        return jdbcTemplate.update(sql,userId);
    }

    @Override
    public User update(long userId, String user_name, String mail, String password) {
        String sql = "UPDATE usersdao set mail=?,user_name=?,password=? where user_id=?";
        jdbcTemplate.update(sql,mail,user_name,password,userId);
        return findById(userId);
    }

    @Override
    public Map<Long, String> getUsernamesByComments(List<Comment> comments) {

        final Map<Long, String> toReturn = new HashMap<>();
        String username;
        if(comments !=null ) {
            for (Comment c : comments) {
                username = findById(c.getUserId()).getUsername();

                toReturn.put(c.getCommentId(), username);
            }
        }

        return toReturn;
    }

    @Override
    public List<String> getMails() {
        final List<String> toReturn = jdbcTemplate.query("SELECT mail FROM users", ROW_MAPPER_MAIL);
        return toReturn;
    }

    @Override
    public List<String> getUserNames() {
        final List<String> toReturn = jdbcTemplate.query("SELECT user_name FROM users", ROW_MAPPER_MAIL);
        return toReturn;

    }
}