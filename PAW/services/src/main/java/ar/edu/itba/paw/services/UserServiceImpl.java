package ar.edu.itba.paw.services;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.VerificationToken;
import ar.edu.itba.paw.models.VerifyUser;
import ar.edu.itba.paw.persistence.UserDao;
import ar.edu.itba.paw.persistence.VerificationTokenDao;
import ar.edu.itba.paw.persistence.VerifyUserDao;
import ar.edu.itba.paw.service.UserAlreadyExistException;
import ar.edu.itba.paw.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    private static final long USER_NOT_EXISTS = -1;
    @Qualifier("userDaoImpl")
    @Autowired
    private UserDao userDao;
    @Autowired
    private VerificationTokenDao tokenDao;
    @Autowired
    private VerifyUserDao verifyUserDao;

    @Autowired
    MessageSource messageSource;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Optional<User> findById(long id) {
        return userDao.findById(id);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    @Override
    public Optional<User> findByMail(String mail) {
        return userDao.findByMail(mail);
    }


    private long checkIfUserExists(String username, String mail) throws UserAlreadyExistException {
        Optional<User> user = findByUsernameOrMail(username,mail);
        if( user.isPresent() ){
            if(user.get().getMail().equals(mail) && user.get().getPassword()==null)
                return user.get().getId();
            if(user.get().getMail().equals(mail)){
                throw new UserAlreadyExistException(messageSource.getMessage("uae.email",new Object[]{user.get().getMail()},Locale.getDefault()));
            }
            throw new UserAlreadyExistException(messageSource.getMessage("uae.username",new Object[]{user.get().getUsername()},Locale.getDefault()));
        }
        return USER_NOT_EXISTS;
    }

    private Optional<User> findByUsernameOrMail(String username, String mail) { return userDao.findByUsernameOrMail(username,mail);
    }

    @Override
    public User create(String username, String mail, String password) throws UserAlreadyExistException{
        long aux= checkIfUserExists(username,mail);
        String psw = passwordEncoder.encode(password);
        if(aux==USER_NOT_EXISTS) {
            return userDao.create(username, mail, psw);
        }
        return update(aux, username, mail, psw).get();
    }


    @Override
    public int delete(long userId) {
        return userDao.delete(userId);
    }

    @Override
    public void  updatePassword(long userId, String password) {
        String psw = passwordEncoder.encode(password);
        userDao.updatePassword(userId,psw);
    }


    private Optional<User> update(long userId, String username, String mail, String password) {
        return userDao.update(userId,username,mail,password);
    }

    @Override
    public void createVerificationToken(User user, String token) {
        Optional<VerificationToken> verificationToken = tokenDao.getByUser(user.getId());
        verificationToken.ifPresent(value -> tokenDao.change(value.getTokenId(), token));
        tokenDao.insert(user.getId(),token);
    }

    @Override
    public Optional<VerificationToken> getVerificationToken(String token) {
        return tokenDao.getByToken(token);
    }

    @Override
    public void saveRegisteredUser(User user) {
        userDao.setEnable(user.getId());
    }

    @Override
    public void generateNewVerificationToken(User user, String token) {
        Optional<VerificationToken> verificationToken = tokenDao.getByUser(user.getId());
        verificationToken.ifPresent(value -> tokenDao.change(value.getTokenId(), token));
    }

    @Override
    public void updateDescription(long userId, String description) {
        userDao.updateDescription(userId,description);
    }

    @Override
    public VerifyUser createVerify(long userId, long frameworkId, long commentId) {
        return verifyUserDao.create(userId,frameworkId,commentId);
    }

    @Override
    public VerifyUser createVerify(long userId, long frameworkId) {
        return verifyUserDao.create(userId,frameworkId);
    }

    @Override
    public List<VerifyUser> getVerifyByUser(long userId, boolean pending) {
        return verifyUserDao.getByUser(userId,pending);
    }

    @Override
    public List<VerifyUser> getVerifyByFramework(long frameworkId, boolean pending) {
        return verifyUserDao.getByFramework(frameworkId,pending);
    }

    @Override
    public List<VerifyUser> getAllVerifyByUser(long userId) {
        return verifyUserDao.getAllByUser(userId);
    }

    @Override
    public List<VerifyUser> getAllVerifyByFramework(long frameworkId) {
        return verifyUserDao.getAllByFramework(frameworkId);
    }

    @Override
    public Optional<VerifyUser> getVerifyById(long verificationId) {
        return verifyUserDao.getById(verificationId);
    }

    @Override
    public List<VerifyUser> getVerifyByPending(boolean pending) {
        return verifyUserDao.getByPending(pending);
    }

    @Override
    public void deleteVerification(long verificationId) {
        verifyUserDao.delete(verificationId);
    }

    @Override
    public void verify(long verificationId) {
        verifyUserDao.verify(verificationId);
    }

    @Override
    public Optional<VerifyUser> getVerifyByFrameworkAndUser(long frameworkId, long userId) {
        return verifyUserDao.getByFrameworkAndUser(frameworkId,userId);
    }

    @Override
    public void passwordMailing(User user, String appUrl) {

            String recipientAddress = user.getMail();

            String token = UUID.randomUUID().toString()+"-a_d-ss-"+user.getId();
            String confirmationUrl = "/recoveringToken?token=" + token;

            String subject = messageSource.getMessage("email.recovery.subject",new Object[]{}, Locale.getDefault());
            String inter_message = messageSource.getMessage("email.recovery.body",new Object[]{}, Locale.getDefault());
            String message = inter_message+ "\r\n" + appUrl + confirmationUrl;

            sendEmail(recipientAddress,subject,message);
        }

    @Override
    public void modMailing(User user, String frameworkName) {
        String subject = messageSource.getMessage("email.moderator.subject",new Object[]{frameworkName}, Locale.getDefault());
        String message = messageSource.getMessage("email.moderator.body",new Object[]{frameworkName}, Locale.getDefault());
        sendEmail(user.getMail(),subject,message);
    }

    private void sendEmail(String recipientAddress,String subject,String message){
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        mailSender.setJavaMailProperties(prop);
        Session session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("confirmemailonly", "pawserver");
            }
        });
        mailSender.setSession(session);
        SimpleMailMessage email = new SimpleMailMessage();
        email.setFrom("confirmemailonly@gmail.com");
        email.setTo(recipientAddress);
        email.setSubject(subject);

        email.setText(message);
        mailSender.send(email);
    }
}
