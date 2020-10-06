package ar.edu.itba.paw;

import ar.edu.itba.paw.models.Content;
import ar.edu.itba.paw.models.ContentTypes;
import ar.edu.itba.paw.models.ContentVote;
import ar.edu.itba.paw.persistence.ContentVoteDao;
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
import static junit.framework.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class ContentVoteDaoTest {
    private static final long CONTENT_ID = 1;
    private static final int POS = 1;
    private static final int NEG = -1;
    private static final long FRAMEWORK_ID = 1;
    private static final long USER_ID = 1;
    private static final String TITLE = "title";
    private static final String TITLE_2 = "title2";
    private static final String LINK = "link";
    private static final ContentTypes TYPE = Enum.valueOf(ContentTypes.class,"book");

    @Autowired
    private DataSource ds;
    @Autowired
    private ContentVoteDao contentVoteDao;

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("content_votes")
                .usingGeneratedKeyColumns("vote_id");

        JdbcTestUtils.deleteFromTables(jdbcTemplate, "content_votes");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "content");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "users");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "frameworks");
        Timestamp ts = new Timestamp(System.currentTimeMillis());

        for (int i = 1; i < 6; i++) {
            jdbcTemplate.execute("insert into users values("+i+",'user"+i+"','mail"+i+"',null,default,default,default,default)");
            jdbcTemplate.execute("insert into frameworks values("+i+",'framework"+i+"','Media','description','introduction',default,'Framework',default,default)");
            jdbcTemplate.execute("insert into content values("+i+","+i+","+i+",'"+TITLE+i+" ','"+ts+"','"+LINK+"','"+TYPE.name()+"')");
        }
    }
    //<editor-fold desc="ContentVote methods">
    @Test
    public void testCreate() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate,"content_votes");

        final ContentVote content = contentVoteDao.insert(CONTENT_ID,USER_ID,POS);

        assertNotNull(content);
        assertEquals(CONTENT_ID, content.getContentId());
        assertEquals(USER_ID,content.getUserId());
        assertEquals(POS, content.getVote());
        assertEquals(true,content.isVoteUp());
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "content_votes","vote_id ="+content.getContentVoteId()));
    }
    @Test(expected = Exception.class)
    public void testCreateOnExisting() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate,"content_votes");
        final Map<String, Object> args = new HashMap<>();
        args.put("content_id", CONTENT_ID);
        args.put("user_id",USER_ID);
        args.put("vote", POS);
        jdbcInsert.execute(args);

        contentVoteDao.insert(CONTENT_ID,USER_ID,POS);
    }

    @Test(expected = Exception.class)
    public void testCreateWithoutUser() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate,"content_votes");
        contentVoteDao.insert(CONTENT_ID,USER_ID+7,POS);
    }
    @Test(expected = Exception.class)
    public void testCreateWithoutContent() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate,"content_votes");
        contentVoteDao.insert(CONTENT_ID+7,USER_ID,POS);
    }

    @Test
    public void testChange() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate,"content_votes");
        final Map<String, Object> args = new HashMap<>();
        args.put("content_id", CONTENT_ID);
        args.put("user_id",USER_ID);
        args.put("vote", POS);
        Number id = jdbcInsert.executeAndReturnKey(args);

        contentVoteDao.update(id.longValue(),NEG);
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "content_votes","vote ="+NEG));
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "content_votes","vote ="+POS));
    }
    @Test
    public void testDelete() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate,"content_votes");
        final Map<String, Object> args = new HashMap<>();
        args.put("content_id", CONTENT_ID);
        args.put("user_id",USER_ID);
        args.put("vote", POS);
        Number id = jdbcInsert.executeAndReturnKey(args);


        int i = contentVoteDao.delete(id.longValue());
        assertEquals(1,i);
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "content_votes","vote_id ="+id.longValue()));
    }
    //</editor-fold>

    //<editor-fold desc="Getters">
    @Test
    public void testGetById() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate,"content_votes");
        final Map<String, Object> args = new HashMap<>();
        args.put("content_id", CONTENT_ID);
        args.put("user_id",USER_ID);
        args.put("vote", POS);
        Number id = jdbcInsert.executeAndReturnKey(args);

        final Optional<ContentVote> content = contentVoteDao.getById(id.longValue());

        assertEquals(true,content.isPresent());
        assertEquals(CONTENT_ID, content.get().getContentId());
        assertEquals(USER_ID,content.get().getUserId());
        assertEquals(POS, content.get().getVote());
        assertEquals(true,content.get().isVoteUp());
    }
    @Test
    public void testGetByIdEmpty() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate,"content_votes");

        final Optional<ContentVote> content = contentVoteDao.getById(1);
        assertEquals(false,content.isPresent());
    }

    @Test
    public void testGetByContentAndUser() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate,"content_votes");
        final Map<String, Object> args = new HashMap<>();
        args.put("content_id", CONTENT_ID);
        args.put("user_id",USER_ID);
        args.put("vote", POS);
        Number id = jdbcInsert.executeAndReturnKey(args);

        final Optional<ContentVote> content = contentVoteDao.getByContentAndUser(CONTENT_ID,USER_ID);

        assertEquals(true,content.isPresent());
        assertEquals(CONTENT_ID, content.get().getContentId());
        assertEquals(USER_ID,content.get().getUserId());
        assertEquals(POS, content.get().getVote());
        assertEquals(true,content.get().isVoteUp());
    }

    //</editor-fold>

}
