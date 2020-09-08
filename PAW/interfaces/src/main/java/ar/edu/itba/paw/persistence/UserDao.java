package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.User;

public interface UserDao {
    User findById(long id);
    User findByUsername(String username);
    User create(String username,String mail,String password);
    User create(String username,String mail);
    int delete(long userId);
    User update(long userId,String username,String mail,String password);

}
