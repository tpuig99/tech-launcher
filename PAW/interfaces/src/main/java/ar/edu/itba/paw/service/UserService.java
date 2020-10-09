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
    Optional<User> findByMail(String mail);
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

    void updatePicture(long userId, byte[] picture);

    VerifyUser createVerify(long userId, long frameworkId, long commentId);

    /** moderator **/
    VerifyUser createVerify(long userId, long frameworkId);
    List<VerifyUser> getVerifyByUser(long userId,boolean pending);
    List<VerifyUser> getVerifyByFramework(long frameworkId,boolean pending);
    List<VerifyUser> getVerifyByFrameworks( List<Long> frameworksIds, boolean pending );
    List<VerifyUser> getAllVerifyByUser(long userId);
    List<VerifyUser> getAllVerifyByFramework(long frameworkId);
    Optional<VerifyUser> getVerifyById(long verificationId);
    List<VerifyUser> getVerifyByPending(boolean pending, long page);
    void deleteVerification(long verificationId);
    void deleteVerificationByUser(long userId);
    void verify(long verificationId);
    Optional<VerifyUser> getVerifyByFrameworkAndUser(long frameworkId, long userId);
    void passwordMailing(User user, String appUrl);
    void modMailing(User user,String frameworkName);
    List<VerifyUser> getApplicantsByPending( boolean pending, long page);
    List<VerifyUser> getApplicantsByFrameworks( List<Long> frameworksIds);
}
