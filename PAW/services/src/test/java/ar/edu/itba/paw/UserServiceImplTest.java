package ar.edu.itba.paw;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.persistence.UserDao;
import ar.edu.itba.paw.services.UserServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest{
        private static final String PASSWORD = "passwordpassword";
        private static final String USERNAME = "username";
        private static final String MAIL = "mail";
        @InjectMocks
        private UserServiceImpl userService = new UserServiceImpl();
        @Mock
        private UserDao mockDao;
        @Test
        public void testCreate() {
// 1. Setup!
        Mockito.when(mockDao.create(Mockito.eq(USERNAME),
        Mockito.eq(MAIL),Mockito.eq(PASSWORD)))
        .thenReturn(new User(1,USERNAME,MAIL, PASSWORD));
// 2. "ejercito" la class under test
        //Optional<User> maybeUser = userService.create(USERNAME, PASSWORD);
        User maybeUser = userService.create(USERNAME,MAIL, PASSWORD);

// 3. Asserts!
        Assert.assertNotNull(maybeUser);
        Assert.assertEquals(USERNAME, maybeUser.getUsername());
        Assert.assertEquals(PASSWORD, maybeUser.getPassword());
        }
@Test
public void testCreateEmptyPassword() {
// 1. Setup!
    Mockito.when(mockDao.create(Mockito.eq(USERNAME),
            Mockito.eq(MAIL)))
            .thenReturn(new User(1,USERNAME,MAIL));
// 2. "ejercito" la class under test
        User maybeUser = userService.create(USERNAME, MAIL);
// 3. Asserts!
        Assert.assertNotNull(maybeUser);
        Assert.assertEquals(USERNAME, maybeUser.getUsername());
        Assert.assertEquals(MAIL, maybeUser.getMail());
        }
}