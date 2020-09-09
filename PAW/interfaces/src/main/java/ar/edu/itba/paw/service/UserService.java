package ar.edu.itba.paw.service;

import ar.edu.itba.paw.models.Comment;
import ar.edu.itba.paw.models.User;

import java.util.List;
import java.util.Map;

public interface UserService {
    User findById(int id);
    User findByUsername(String username);
    User create(String username,String mail);
    User create(String username,String mail,String password);
    int delete(long userId);
    User update(long userId,String username,String mail,String password);
    Map<Long, String> getUsernamesByComments(List<Comment> comments);

}
