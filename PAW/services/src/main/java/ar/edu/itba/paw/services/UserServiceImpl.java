package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Comment;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.VerificationToken;
import ar.edu.itba.paw.persistence.UserDao;
import ar.edu.itba.paw.persistence.VerificationTokenDao;
import ar.edu.itba.paw.service.UserAlreadyExistException;
import ar.edu.itba.paw.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    private static long USER_NOT_EXISTS = -1;
    @Qualifier("userDaoImpl")
    @Autowired
    private UserDao userDao;
    @Autowired
    private VerificationTokenDao tokenDao;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User findById(int id) {
        return userDao.findById(id);
    }

    @Override
    public User findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    @Override
    public User findByUsernameForLogin(String username) {
        User user = findByUsername(username);
        if(user.isEnable())
            return user;
        return null;
    }

    @Override
    public User create(String username,String mail) throws UserAlreadyExistException {
        checkIfUserExists(username,mail);

        return userDao.create(username,mail);
    }

    private long checkIfUserExists(String username, String mail) throws UserAlreadyExistException {
        User user = findByUsernameOrMail(username,mail);
        if( user != null ){
            if(user.getMail().equals(mail) && user.getPassword()==null)
                return user.getId();
            if(user.getMail().equals(mail)){
                throw new UserAlreadyExistException("There is an account with that email address: " +  user.getMail());
            }
            throw new UserAlreadyExistException("There is an account with that username " +  user.getUsername());
        }
        return USER_NOT_EXISTS;
    }

    private User findByUsernameOrMail(String username, String mail) { return userDao.findByUsernameOrMail(username,mail);
    }

    @Override
    public User create(String username, String mail, String password) throws UserAlreadyExistException{
        long aux= checkIfUserExists(username,mail);
        String psw = passwordEncoder.encode(password);
        if(aux==USER_NOT_EXISTS)
            return userDao.create(username,mail,psw);
        userDao.update(aux,username,mail,psw);
        return userDao.findById(aux);
    }

    @Override
    public int delete(long userId) {
        return userDao.delete(userId);
    }

    @Override
    public User update(long userId, String username, String mail, String password) {
        return userDao.update(userId,username,mail,password);
    }

    @Override
    public Map<Long, String> getUsernamesByComments(List<Comment> comments) {
        return userDao.getUsernamesByComments(comments);
    }

    @Override
    public void createVerificationToken(User user, String token) {
        VerificationToken verificationToken = tokenDao.getByUser(user.getId());
        if(verificationToken!=null){
            tokenDao.change(verificationToken.getTokenId(),token);
        }
        tokenDao.insert(user.getId(),token);
    }

    @Override
    public VerificationToken getVerificationToken(String token) {
        return tokenDao.getByToken(token);
    }

    @Override
    public void saveRegisteredUser(User user) {
        userDao.setEnable(user.getId());
    }

    @Override
    public void generateNewVerificationToken(User user, String token) {
        VerificationToken verificationToken = tokenDao.getByUser(user.getId());
        tokenDao.change(verificationToken.getTokenId(),token);
    }
}
