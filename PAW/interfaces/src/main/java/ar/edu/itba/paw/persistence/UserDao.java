package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Comment;
import ar.edu.itba.paw.models.User;

import java.util.List;
import java.util.Map;

public interface UserDao {
    List<User> getAll();
    User findById(long id);
    User findByUsername(String username);
    User findByUsernameOrMail(String username,String Mail);
    User findByMail(String mail);
    User create(String username,String mail,String password);
    User create(String username,String mail);
    int delete(long userId);
    User update(long userId,String username,String mail,String password);
    Map<Long, String> getUsernamesByComments(List<Comment> comments);
    List<String> getMails();
    List<String> getUserNames();

}
