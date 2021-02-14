package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Framework;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.VerificationToken;
import ar.edu.itba.paw.models.VerifyUser;
import ar.edu.itba.paw.persistence.UserDao;
import ar.edu.itba.paw.persistence.VerificationTokenDao;
import ar.edu.itba.paw.persistence.VerifyUserDao;
import ar.edu.itba.paw.service.UserAlreadyExistException;
import ar.edu.itba.paw.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.UUID;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

@Service
public class UserServiceImpl implements UserService {
    private final long PAGE_SIZE=5;
    private static final long USER_NOT_EXISTS = -1;
    private static final int DELETE_VERIFICATIONS = -1;
    private static final int ALLOW_MOD = 1;

    @Autowired
    private UserDao userDao;

    @Autowired
    private VerificationTokenDao tokenDao;

    @Autowired
    private VerifyUserDao verifyUserDao;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    @Override
    public Optional<User> findById(long id) {
        return userDao.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<User> findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<User> findByMail(String mail) {
        return userDao.findByMail(mail);
    }

    private long geUserIdIfNotExists(String username, String mail) throws UserAlreadyExistException {
        Optional<User> user = findByUsernameOrMail(username,mail);
        if( user.isPresent() ){
            if(user.get().getMail().equals(mail) && user.get().getPassword()==null)
                return user.get().getId();
            if(user.get().getMail().equals(mail)){
                throw new UserAlreadyExistException(messageSource.getMessage("uae.email",new Object[]{user.get().getMail()}, LocaleContextHolder.getLocale()));
            }
            throw new UserAlreadyExistException(messageSource.getMessage("uae.username",new Object[]{user.get().getUsername()},LocaleContextHolder.getLocale()));
        }
        return USER_NOT_EXISTS;
    }

    private Optional<User> findByUsernameOrMail(String username, String mail) { return userDao.findByUsernameOrMail(username,mail);
    }

    @Transactional
    @Override
    public User create(String username, String mail, String password) throws UserAlreadyExistException{
        long aux= geUserIdIfNotExists(username,mail);
        String psw = passwordEncoder.encode(password);
        if(aux==USER_NOT_EXISTS) {
            return userDao.create(username, mail, psw);
        }
        return update(aux, username, mail, psw).get();
    }

    @Transactional
    @Override
    public void delete(long userId) {
        userDao.delete(userId);
    }

    @Transactional
    @Override
    public void  updatePassword(long userId, String password) {
        String psw = passwordEncoder.encode(password);
        userDao.updatePassword(userId,psw);
    }

    @Transactional
    @Override
    public int updateModAllow(long userId, boolean allow) {
        userDao.updateModAllow(userId, allow);
        if(!allow){
            deleteVerificationByUser(userId);
            return DELETE_VERIFICATIONS;
        }
        return ALLOW_MOD;
    }

    private Optional<User> update(long userId, String username, String mail, String password) {
        return userDao.update(userId,username,mail,password);
    }

    @Transactional
    @Override
    public void createVerificationToken(User user, String token,String appUrl) {
        tokenDao.insert(user.getId(),token);
        String message = messageSource.getMessage("email.body",new Object[]{}, LocaleContextHolder.getLocale()) +
                "\r\n" +
                appUrl + "/confirm/" + token;
        sendEmail(user.getMail(),messageSource.getMessage("email.subject",new Object[]{}, LocaleContextHolder.getLocale()),message);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<VerificationToken> getVerificationToken(String token) {
        return tokenDao.getByToken(token);
    }

    @Transactional
    @Override
    public void saveRegisteredUser(User user) {
        userDao.setEnable(user.getId());
    }

    @Transactional
    @Override
    public void generateNewVerificationToken(User user, String token,String appUrl) {
        Optional<VerificationToken> verificationToken = user.getVerificationToken();
        verificationToken.ifPresent(value -> tokenDao.change(value, token));
        String message = messageSource.getMessage("email.body",new Object[]{}, LocaleContextHolder.getLocale()) +
                "\r\n" +
                appUrl + "/confirm/" + token;
        sendEmail(user.getMail(),messageSource.getMessage("email.subject",new Object[]{}, LocaleContextHolder.getLocale()),message);
    }

    @Transactional
    @Override
    public boolean quitModdingFromTech(User user, long frameworkId) {
        for( VerifyUser verifyUser : user.getVerifications() ){
            if (verifyUser.getFrameworkId() == frameworkId) {
                deleteVerification(verifyUser.getVerificationId());
                return true;
            }
        }
        return false;
    }

    @Transactional
    @Override
    public VerifyUser createVerify(User user, Framework framework) {
        return verifyUserDao.create(user,framework,null);
    }


    @Transactional(readOnly = true)
    @Override
    public List<VerifyUser> getVerifyByFrameworks(List<Long> frameworksIds, boolean pending, long page) {
        return verifyUserDao.getVerifyForCommentByFrameworks(frameworksIds, pending, page, PAGE_SIZE);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<VerifyUser> getVerifyById(long verificationId) {
        return verifyUserDao.getById(verificationId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<VerifyUser> getVerifyByPending(boolean pending, long page) {
        return verifyUserDao.getVerifyForCommentByPending(pending, page, PAGE_SIZE);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Integer> getVerifyByPendingAmount(boolean pending) {
        return verifyUserDao.getVerifyForCommentByPendingAmount(pending);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Integer> getVerifyByFrameworkAmount(List<Long> frameworksIds, boolean pending) {
        return verifyUserDao.getVerifyForCommentByFrameworkAmount(frameworksIds,pending);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Integer> getApplicantsByPendingAmount(boolean pending) {
        return verifyUserDao.getApplicantsByPendingAmount(pending);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Integer> getApplicantsByFrameworkAmount(List<Long> frameworksIds, boolean pending) {
        return verifyUserDao.getApplicantsByFrameworkAmount(frameworksIds,pending);
    }

    @Transactional
    @Override
    public void deleteVerification(long verificationId) {
        verifyUserDao.delete(verificationId);
    }

    @Transactional
    @Override
    public void deleteVerificationByUser(long userId) {
        verifyUserDao.deleteVerificationByUser(userId);
    }

    @Transactional
    @Override
    public void verify(long verificationId) {
        verifyUserDao.verify(verificationId);
    }

    @Override
    public void passwordMailing(User user, String appUrl) {

        String recipientAddress = user.getMail();

        String token = UUID.randomUUID().toString()+"-a_d-ss-"+user.getId();
        String confirmationUrl = "/forgot_password/" + token;

        String subject = messageSource.getMessage("email.recovery.subject",new Object[]{}, LocaleContextHolder.getLocale());
        String inter_message = messageSource.getMessage("email.recovery.body",new Object[]{}, LocaleContextHolder.getLocale());
        String message = inter_message+ "\r\n" + appUrl + confirmationUrl;

        sendEmail(recipientAddress,subject,message);
    }

    @Override
    public void modMailing(User user, String frameworkName) {
        String subject = messageSource.getMessage("email.moderator.subject",new Object[]{frameworkName}, LocaleContextHolder.getLocale());
        String message = messageSource.getMessage("email.moderator.body",new Object[]{frameworkName}, LocaleContextHolder.getLocale());
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

    @Transactional(readOnly = true)
    @Override
    public List<VerifyUser> getApplicantsByPending(boolean pending, long page) {
        return verifyUserDao.getApplicantsByPending(true, page, PAGE_SIZE);
    }

    @Transactional(readOnly = true)
    @Override
    public List<VerifyUser> getApplicantsByFrameworks(List<Long> frameworksIds, long page) {
        return verifyUserDao.getApplicantsByFrameworks(frameworksIds, page, PAGE_SIZE);
    }

    @Transactional
    @Override
    public void updateInformation(Long userId, String description, byte[] picture, boolean updatePicture) {
        userDao.updateInformation(userId, description, picture, updatePicture);
    }

    @Transactional(readOnly = true)
    @Override
    public List<VerifyUser> getVerifyByPendingAndFrameworks( boolean pending, List<Long> frameworkIds, long page ){
        return verifyUserDao.getVerifyByPendingAndFramework( pending, frameworkIds, page, PAGE_SIZE);
    }

    @Transactional(readOnly = true)
    @Override
    public Integer getVerifyByPendingAndFrameworksAmount(boolean pending, List<Long> frameworkIds){
        return verifyUserDao.getVerifyByPendingAndFrameworksAmount( pending, frameworkIds);
    }
}
