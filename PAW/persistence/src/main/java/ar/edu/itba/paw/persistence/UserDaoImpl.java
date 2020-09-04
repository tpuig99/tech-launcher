package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.User;
import org.springframework.stereotype.Repository;

@Repository
public class UserDaoImpl implements UserDao {

    @Override
    public User findById(long id){
        return new User(id, "PAW from DAO");
    }

    @Override
    public User create(String username) {
        return null;
    }


}