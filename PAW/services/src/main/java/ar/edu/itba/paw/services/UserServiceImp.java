package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.interfaces.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImp implements UserService {
    public UserServiceImp() {
    }

    @Override
    public User findById(int id) {
        return new User(id,"PAW");
    }
}
