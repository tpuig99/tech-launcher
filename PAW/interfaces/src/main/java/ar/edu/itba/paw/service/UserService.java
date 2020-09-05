package ar.edu.itba.paw.service;

import ar.edu.itba.paw.models.User;

public interface UserService {
    public User findById(int id);
    /**
     * Create a new user.
     *
     * @param username The name of the user.
     * @return The created user.
     */
    User create(String username,String mail);
    User deleteUser(long userId);
    User changeUser(long userId,String username,String mail,String password);


}
