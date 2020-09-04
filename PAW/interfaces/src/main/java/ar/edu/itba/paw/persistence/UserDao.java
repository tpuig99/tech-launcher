package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.User;

public interface UserDao {
    public User findById(long id);

    User create(String username);
}
