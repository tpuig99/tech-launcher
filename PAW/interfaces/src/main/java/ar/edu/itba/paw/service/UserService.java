package ar.edu.itba.paw.service;

import ar.edu.itba.paw.models.Comment;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.VerificationToken;
import ar.edu.itba.paw.models.VerifyUser;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserService {
    /** user **/
    Optional<User> findById(long id);
    Optional<User> findByUsername(String username);
    User create(String username,String mail,String password) throws UserAlreadyExistException;
    int delete(long userId);
    void updateDescription(long userId,String description);
    void updatePassword(long userId,String password);
    void updateModAllow(long userId, boolean allow);

    /** register **/
    void createVerificationToken(User user, String token);
    Optional<VerificationToken> getVerificationToken(String token);
    void saveRegisteredUser(User user);
    void generateNewVerificationToken(User user, String token);

    /** moderator **/
    VerifyUser createVerify(long userId, long frameworkId);
    List<VerifyUser> getVerifyByUser(long userId,boolean pending);
    List<VerifyUser> getVerifyByFramework(long frameworkId,boolean pending);
    List<VerifyUser> getAllVerifyByUser(long userId);
    List<VerifyUser> getAllVerifyByFramework(long frameworkId);
    Optional<VerifyUser> getVerifyById(long verificationId);
    List<VerifyUser> getVerifyByPending(boolean pending);
    void deleteVerification(long verificationId);
    void deleteVerificationByUser(long userId);
    void verify(long verificationId);
    Optional<VerifyUser> getVerifyByFrameworkAndUser(long frameworkId, long userId);
}
