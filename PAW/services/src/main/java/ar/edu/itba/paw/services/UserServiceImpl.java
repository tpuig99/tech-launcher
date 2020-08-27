package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.interfaces.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    public UserServiceImpl() {
    }

    @Override
    public User findById(int id) {
        return new User(id,"PAW");
    }
}
