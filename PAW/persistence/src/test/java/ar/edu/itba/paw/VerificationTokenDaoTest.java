package ar.edu.itba.paw;

import ar.edu.itba.paw.models.Content;
import ar.edu.itba.paw.models.VerificationToken;
import ar.edu.itba.paw.persistence.ReportContentDao;
import ar.edu.itba.paw.persistence.VerificationTokenDao;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static junit.framework.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class VerificationTokenDaoTest {
    private static final long USER_ID = 1;
    private static final String TOKEN = "token";
    private static final String TOKEN_2 = "token2";

    @Autowired
    private DataSource ds;
    @Autowired
    private VerificationTokenDao verificationTokenDao;

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("verification_token")
                .usingGeneratedKeyColumns("token_id");

        JdbcTestUtils.deleteFromTables(jdbcTemplate, "verification_token");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "users");

        for (int i = 1; i < 7; i++) {
            jdbcTemplate.execute("insert into users values("+i+",'user"+i+"','mail"+i+"',null,default,default,default,default)");
           }
    }

    //<editor-fold desc="Content Methods">
    @Test
    public void testCreate() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "verification_token");

        verificationTokenDao.insert(USER_ID,TOKEN);
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "verification_token","user_id ="+USER_ID));
    }

    @Test(expected = Exception.class)
    public void testCreateOnExisting() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "verification_token");
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        final Map<String, Object> args = new HashMap<>();
        args.put("user_id",USER_ID);
        args.put("token", TOKEN);
        args.put("exp_date",ts);
        jdbcInsert.execute(args);

        verificationTokenDao.insert(USER_ID,TOKEN);
    }
    @Test(expected = Exception.class)
    public void testCreateWithoutUser() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "verification_token");
        verificationTokenDao.insert(USER_ID+10,TOKEN);
    }

    /*@Test
    public void testChange() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "verification_token");
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        final Map<String, Object> args = new HashMap<>();
        args.put("user_id",USER_ID);
        args.put("token", TOKEN);
        args.put("exp_date",ts);
        Number id = jdbcInsert.executeAndReturnKey(args);
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setExpiryDay(ts);
        verificationToken.setToken(TOKEN);
        verificationToken.setTokenId(id.longValue());
        verificationTokenDao.change(id.longValue(),TOKEN_2);
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "verification_token","token = '"+TOKEN_2+"'"));
    }*/
    //</editor-fold>
    //<editor-fold desc="Getters">

    @Test
    public void testGetByToken() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate,"content");
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        final Map<String, Object> args = new HashMap<>();
        args.put("user_id",USER_ID);
        args.put("token", TOKEN);
        args.put("exp_date",ts);
        Number id = jdbcInsert.executeAndReturnKey(args);
        Optional<VerificationToken> vt = verificationTokenDao.getByToken(TOKEN);

        assertEquals(true,vt.isPresent());
        assertEquals(TOKEN, vt.get().getToken());
        assertEquals(USER_ID,vt.get().getUserId());
        assertEquals(id, vt.get().getTokenId());
    }
    @Test
    public void testGetByTokenNotExisting() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate,"content");
        Optional<VerificationToken> vt = verificationTokenDao.getByToken(TOKEN);

        assertEquals(false,vt.isPresent());
    }
    //</editor-fold>
}
