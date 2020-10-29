package ar.edu.itba.paw.service;

import ar.edu.itba.paw.models.Framework;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.VerificationToken;
import ar.edu.itba.paw.models.VerifyUser;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

public interface UserService {
    /** user **/
    Optional<User> findById(long id);
    Optional<User> findByUsername(String username);
    Optional<User> findByMail(String mail);
    User create(String username,String mail,String password) throws UserAlreadyExistException;
    void delete(long userId);
    boolean quitModdingFromTech(User user, long frameworkId);
    void updateDescription(long userId, String description);
    void updatePassword(long userId,String password);
    void updateModAllow(long userId, boolean allow);

    /** register **/
    void createVerificationToken(User user, String token,String appUrl);
    Optional<VerificationToken> getVerificationToken(String token);
    void saveRegisteredUser(User user);
    void generateNewVerificationToken(User user, String token, String appUrl);
    void internalLogin(String user, String pass, HttpServletRequest req);
    void updatePicture(long userId, byte[] picture);

    /** moderator **/
    VerifyUser createVerify(User user, Framework framework);
    List<VerifyUser> getVerifyByUser(long userId,boolean pending);
    List<VerifyUser> getVerifyByFramework(long frameworkId,boolean pending);
    List<VerifyUser> getVerifyByFrameworks( List<Long> frameworksIds, boolean pending, long page );
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
    List<VerifyUser> getApplicantsByFrameworks( List<Long> frameworksIds, long page);
}
