package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Comment;
import ar.edu.itba.paw.models.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserDao {
    List<User> getAll();
    Optional<User> findById(long id);
    Optional<User> findByUsername(String username);
    Optional<User> findByUsernameOrMail(String username,String Mail);
    Optional<User> findByMail(String mail);
    User create(String username,String mail,String password);
    User create(String username,String mail);
    int delete(long userId);
    Optional<User> update(long userId, String username, String mail, String password);
    Map<Long, String> getUsernamesByComments(List<Comment> comments);
    List<String> getMails();
    List<String> getUserNames();
    void setEnable(long id);
    void updateDescription(long userId,String description);
}
