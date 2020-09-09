package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Comment;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.persistence.UserDao;
import ar.edu.itba.paw.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Qualifier("userDaoImpl")
    @Autowired
    private UserDao userDao;

    @Override
    public User findById(int id) {
        return userDao.findById(id);
    }

    @Override
    public User findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    @Override
    public User create(String username,String mail) {
        User user = findByUsername(username);
        if( user != null ){
            return user;
        }
        return userDao.create(username,mail);

    }

    @Override
    public User create(String username, String mail, String password) {
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
}
