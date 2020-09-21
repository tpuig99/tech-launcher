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
    private PasswordEncoder passwordEncoder;

    @Override
    public Optional<User> findById(int id) {
        return userDao.findById(id);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    @Override
    public Optional<User> findByUsernameForLogin(String username) {
        return findByUsername(username);
    }

    @Override
    public User create(String username,String mail) throws UserAlreadyExistException {
        checkIfUserExists(username,mail);

        return userDao.create(username,mail);
    }

    private long checkIfUserExists(String username, String mail) throws UserAlreadyExistException {
        Optional<User> user = findByUsernameOrMail(username,mail);
        if( user.isPresent() ){
            if(user.get().getMail().equals(mail) && user.get().getPassword()==null)
                return user.get().getId();
            if(user.get().getMail().equals(mail)){
                throw new UserAlreadyExistException("There is an account with that email address: " +  user.get().getMail());
            }
            throw new UserAlreadyExistException("There is an account with that username " +  user.get().getUsername());
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
    public Optional<User> update(long userId, String username, String mail, String password) {
        return userDao.update(userId,username,mail,password);
    }

    @Override
    public Map<Long, String> getUsernamesByComments(List<Comment> comments) {
        return userDao.getUsernamesByComments(comments);
    }

    @Override
    public Map<Long, String> getUsernamesByReplies(Map<Long, List<Comment>> map) {
        List<Comment> commentList = new ArrayList<>();
        for (List<Comment> mapElement : map.values()) {
            commentList.addAll(mapElement);
        }
        return userDao.getUsernamesByReplies(commentList);
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
}
