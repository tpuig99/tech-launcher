package ar.edu.itba.paw;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.persistence.UserDao;
import ar.edu.itba.paw.persistence.VerifyUserDao;
import ar.edu.itba.paw.service.UserAlreadyExistException;
import ar.edu.itba.paw.services.UserServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static junit.framework.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {
    private static final String PASSWORD = "passwordpassword";
    private static final String USERNAME = "username";
    private static final String MAIL = "username@mail.com";
    @InjectMocks
    private UserServiceImpl contentService = new UserServiceImpl();
    @Mock
    private UserDao mockDao;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private VerifyUserDao verifyUserDao;

    @Test()
    public void createNew() throws UserAlreadyExistException {
        Mockito.when(passwordEncoder.encode(PASSWORD)).thenReturn(PASSWORD);
        Mockito.when(mockDao.create(USERNAME,MAIL,PASSWORD)).thenReturn(new User(0,USERNAME,MAIL,PASSWORD,false,"",true,false,null,null));
        Mockito.when(mockDao.findByUsernameOrMail(USERNAME,MAIL)).thenReturn(Optional.empty());

        User user = contentService.create(USERNAME,MAIL,PASSWORD);

        assertEquals(USERNAME,user.getUsername());
        assertEquals(MAIL,user.getMail());
        assertEquals(PASSWORD,user.getPassword());
    }
    @Test()
    public void createNewNotActivated() throws UserAlreadyExistException {
        Mockito.when(passwordEncoder.encode(PASSWORD)).thenReturn(PASSWORD);
        Mockito.when(mockDao.update(0,USERNAME,MAIL,PASSWORD)).thenReturn(Optional.of(new User(0,USERNAME,MAIL,PASSWORD,false,"",true,false,null,null)));
        Mockito.when(mockDao.findByUsernameOrMail(USERNAME,MAIL)).thenReturn(Optional.of(new User(0,USERNAME,MAIL,null,false,"",true,false,null,null)));

        User user = contentService.create(USERNAME,MAIL,PASSWORD);

        assertEquals(USERNAME,user.getUsername());
        assertEquals(MAIL,user.getMail());
        assertEquals(PASSWORD,user.getPassword());
    }
    @Test(expected = Exception.class)
    public void createNewExisting() throws UserAlreadyExistException {
        Mockito.when(mockDao.findByUsernameOrMail(USERNAME,MAIL)).thenReturn(Optional.of(new User(0,USERNAME,MAIL,PASSWORD,true,"",true,false,null,null)));

        contentService.create(USERNAME,MAIL,PASSWORD);
    }
    @Test(expected = Exception.class)
    public void UpdateModeFalse(){
        Mockito.doThrow(new RuntimeException()).when(verifyUserDao).deleteVerificationByUser(0);
        contentService.updateModAllow(0,false);
    }

}
