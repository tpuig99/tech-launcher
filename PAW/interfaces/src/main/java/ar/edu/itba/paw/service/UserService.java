package ar.edu.itba.paw.service;

import ar.edu.itba.paw.models.Comment;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.VerificationToken;

import java.util.List;
import java.util.Map;

public interface UserService {
    User findById(int id);
    User findByUsername(String username);
    User create(String username,String mail) throws UserAlreadyExistException;
    User create(String username,String mail,String password) throws UserAlreadyExistException;;
    int delete(long userId);
    User update(long userId,String username,String mail,String password);
    Map<Long, String> getUsernamesByComments(List<Comment> comments);
    void createVerificationToken(User user, String token);
    VerificationToken getVerificationToken(String token);
    void saveRegisteredUser(User user);
    void generateNewVerificationToken(User user, String token);
}
