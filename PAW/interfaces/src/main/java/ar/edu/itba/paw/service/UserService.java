package ar.edu.itba.paw.service;

import ar.edu.itba.paw.models.Comment;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.VerificationToken;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserService {
    Optional<User> findById(int id);
    Optional<User> findByUsername(String username);
    Optional<User> findByUsernameForLogin(String username);
    User create(String username,String mail) throws UserAlreadyExistException;
    User create(String username,String mail,String password) throws UserAlreadyExistException;
    int delete(long userId);
    Optional<User> update(long userId,String username,String mail,String password);
    Map<Long, String> getUsernamesByComments(List<Comment> comments);
    Map<Long, String> getUsernamesByReplies(Map<Long, List<Comment>> map);
    void createVerificationToken(User user, String token);
    Optional<VerificationToken> getVerificationToken(String token);
    void saveRegisteredUser(User user);
    void generateNewVerificationToken(User user, String token);
    void updateDescription(long userId,String description);
}
