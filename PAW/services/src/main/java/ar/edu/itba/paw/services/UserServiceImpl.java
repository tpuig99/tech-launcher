package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.persistence.UserDao;
import ar.edu.itba.paw.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

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
    public User create(String username,String mail) {
        return userDao.create(username,mail);

    }

    @Override
    public User deleteUser(long userId) {
        return userDao.deleteUser(userId);
    }

    @Override
    public User changeUser(long userId, String username, String mail, String password) {
        return userDao.changeUser(userId,username,mail,password);
    }
}
