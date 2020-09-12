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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Qualifier("userDaoImpl")
    @Autowired
    private UserDao userDao;
    @Autowired
    private VerificationTokenDao tokenDao;

    @Override
    public User findById(int id) {
        return userDao.findById(id);
    }

    @Override
    public User findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    @Override
    public User create(String username,String mail) throws UserAlreadyExistException {
        checkIfUserExists(username,mail);
        return userDao.create(username,mail);
    }

    private void checkIfUserExists(String username, String mail) throws UserAlreadyExistException {
        User user = findByUsernameOrMail(username,mail);
        if( user != null ){
            if(user.getMail().equals(mail)){
                throw new UserAlreadyExistException("There is an account with that email address: " +  user.getMail());
            }
            throw new UserAlreadyExistException("There is an account with that username " +  user.getUsername());
        }
    }

    private User findByUsernameOrMail(String username, String mail) { return userDao.findByUsernameOrMail(username,mail);
    }

    @Override
    public User create(String username, String mail, String password) throws UserAlreadyExistException{
        checkIfUserExists(username,mail);
        return userDao.create(username,mail,password);
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
