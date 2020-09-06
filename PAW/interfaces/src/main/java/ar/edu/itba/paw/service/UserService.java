package ar.edu.itba.paw.service;

import ar.edu.itba.paw.models.User;

public interface UserService {
    User findById(int id);
    User create(String username,String mail);
    User create(String username,String mail,String password);
    int delete(long userId);
    User update(long userId,String username,String mail,String password);


}
