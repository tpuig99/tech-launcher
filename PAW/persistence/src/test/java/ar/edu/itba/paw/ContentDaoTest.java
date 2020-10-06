package ar.edu.itba.paw;

import ar.edu.itba.paw.models.Content;
import ar.edu.itba.paw.models.ContentTypes;
import ar.edu.itba.paw.persistence.ContentDao;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;

import javax.sql.DataSource;


import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class ContentDaoTest {
    private static final long FRAMEWORK_ID = 1;
    private static final long USER_ID = 1;
    private static final String TITLE = "title";
    private static final String TITLE_2 = "title2";
    private static final String LINK = "link";
    private static final ContentTypes TYPE = Enum.valueOf(ContentTypes.class,"book");

    @Autowired
    private DataSource ds;
    @Autowired
    private ContentDao contentDao;

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("content")
                .usingGeneratedKeyColumns("content_id");

        JdbcTestUtils.deleteFromTables(jdbcTemplate, "content");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "users");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "frameworks");

        for (int i = 1; i < 6; i++) {
            jdbcTemplate.execute("insert into users values("+i+",'user"+i+"','mail"+i+"',null,default,default,default,default)");
            jdbcTemplate.execute("insert into frameworks values("+i+",'framework"+i+"','Media','description','introduction',default,'Framework',default,default)");
        }

    }

    //<editor-fold desc="Content Methods">
    @Test
    public void testCreate() {
        JdbcTestUtils.deleteFromTableWhere(jdbcTemplate,"content","title = "+TITLE);

        final Content content = contentDao.insertContent(FRAMEWORK_ID,USER_ID,TITLE,LINK,TYPE);

        assertNotNull(content);
        assertEquals(FRAMEWORK_ID, content.getFrameworkId());
        assertEquals(USER_ID,content.getUserId());
        assertEquals(TITLE, content.getTitle());
        assertEquals(TYPE,content.getType());
        assertEquals(LINK,content.getLink());
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "content","content_id ="+content.getContentId()));
    }
    @Test(expected = Exception.class)
    public void testCreateOnExisting() {
        JdbcTestUtils.deleteFromTableWhere(jdbcTemplate,"content","title = ",TITLE);
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        final Map<String, Object> args = new HashMap<>();
        args.put("framework_id", FRAMEWORK_ID);
        args.put("user_id",USER_ID);
        args.put("title", TITLE);
        args.put("tstamp", ts);
        args.put("link", LINK);
        args.put("type", TYPE.name());
        jdbcInsert.execute(args);

        final Content content = contentDao.insertContent(FRAMEWORK_ID,USER_ID,TITLE,LINK,TYPE);
    }
    @Test(expected = Exception.class)
    public void testCreateWithoutUser() {
        JdbcTestUtils.deleteFromTableWhere(jdbcTemplate,"content","title = ",TITLE);
        final Content content = contentDao.insertContent(FRAMEWORK_ID,USER_ID+1,TITLE,LINK,TYPE);
    }
    @Test(expected = Exception.class)
    public void testCreateWithoutFramework() {
        JdbcTestUtils.deleteFromTableWhere(jdbcTemplate,"content","title = ",TITLE);
        final Content content = contentDao.insertContent(FRAMEWORK_ID+1,USER_ID,TITLE,LINK,TYPE);
    }
    @Test
    public void testChange() {
        JdbcTestUtils.deleteFromTableWhere(jdbcTemplate,"content","title = "+TITLE);
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        final Map<String, Object> args = new HashMap<>();
        args.put("framework_id", FRAMEWORK_ID);
        args.put("user_id",USER_ID);
        args.put("title", TITLE);
        args.put("tstamp", ts);
        args.put("link", LINK);
        args.put("type", TYPE.name());
        Number contentId = jdbcInsert.executeAndReturnKey(args);

        Optional<Content> content = contentDao.changeContent(contentId.longValue(),TITLE_2,LINK,TYPE);
        assertEquals(true,content.isPresent());
        assertEquals(FRAMEWORK_ID, content.get().getFrameworkId());
        assertEquals(USER_ID,content.get().getUserId());
        assertEquals(TITLE_2, content.get().getTitle());
        assertEquals(TYPE,content.get().getType());
        assertEquals(LINK,content.get().getLink());
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "content","content_id ="+content.get().getContentId()));
    }
    @Test
    public void testDelete() {
        JdbcTestUtils.deleteFromTableWhere(jdbcTemplate,"content","title = "+TITLE);
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        final Map<String, Object> args = new HashMap<>();
        args.put("framework_id", FRAMEWORK_ID);
        args.put("user_id",USER_ID);
        args.put("title", TITLE);
        args.put("tstamp", ts);
        args.put("link", LINK);
        args.put("type", TYPE.name());
        Number contentId = jdbcInsert.executeAndReturnKey(args);

        int i = contentDao.deleteContent(contentId.longValue());
        assertEquals(1,i);
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "content","content_id ="+contentId));
    }

    //</editor-fold>
    //<editor-fold desc="Getters">
    @Test
    public void testGetById() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate,"content");
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        final Map<String, Object> args = new HashMap<>();
        args.put("framework_id", FRAMEWORK_ID);
        args.put("user_id",USER_ID);
        args.put("title", TITLE);
        args.put("tstamp", ts);
        args.put("link", LINK);
        args.put("type", TYPE.name());
        Number contentId = jdbcInsert.executeAndReturnKey(args);

        Optional<Content> content = contentDao.getById(contentId.longValue());

        assertEquals(true,content.isPresent());
        assertEquals(FRAMEWORK_ID, content.get().getFrameworkId());
        assertEquals(USER_ID,content.get().getUserId());
        assertEquals(TITLE, content.get().getTitle());
        assertEquals(TYPE,content.get().getType());
        assertEquals(LINK,content.get().getLink());
    }
    @Test()
    public void testGetByIdNotExisting() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate,"content");

        Optional<Content> content = contentDao.getById(1);

        assertEquals(false,content.isPresent());
    }
    @Test
    public void testGetByFramework() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate,"content");
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        for (int i = 0; i < 3; i++) {
            final Map<String, Object> args = new HashMap<>();
            args.put("framework_id", FRAMEWORK_ID);
            args.put("user_id",USER_ID+i);
            args.put("title", TITLE+i);
            args.put("tstamp", ts);
            args.put("link", LINK);
            args.put("type", TYPE.name());
            jdbcInsert.executeAndReturnKey(args);
        }
        final Map<String, Object> args = new HashMap<>();
        args.put("framework_id", FRAMEWORK_ID+4);
        args.put("user_id",USER_ID+4);
        args.put("title", TITLE);
        args.put("tstamp", ts);
        args.put("link", LINK);
        args.put("type", TYPE.name());
        jdbcInsert.executeAndReturnKey(args);

        List<Content> contents = contentDao.getContentByFramework(FRAMEWORK_ID);

        assertEquals(false,contents.isEmpty());
        assertEquals(3,contents.size());
        for (Content c:contents) {
            assertEquals(FRAMEWORK_ID, c.getFrameworkId());
        }
    }

    @Test
    public void testGetByFrameworkEmpty() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate,"content");
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        for (int i = 0; i < 3; i++) {
            final Map<String, Object> args = new HashMap<>();
            args.put("framework_id", FRAMEWORK_ID);
            args.put("user_id",USER_ID+i);
            args.put("title", TITLE+i);
            args.put("tstamp", ts);
            args.put("link", LINK);
            args.put("type", TYPE.name());
            jdbcInsert.executeAndReturnKey(args);
        }

        List<Content> contents = contentDao.getContentByFramework(FRAMEWORK_ID+1);

        assertEquals(true,contents.isEmpty());
    }
    @Test
    public void testGetByFrameworkAndUser() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate,"content");
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        for (int i = 0; i < 3; i++) {
            final Map<String, Object> args = new HashMap<>();
            args.put("framework_id", FRAMEWORK_ID);
            args.put("user_id",USER_ID);
            args.put("title", TITLE+i);
            args.put("tstamp", ts);
            args.put("link", LINK);
            args.put("type", TYPE.name());
            jdbcInsert.executeAndReturnKey(args);
        }
        final Map<String, Object> args = new HashMap<>();
        args.put("framework_id", FRAMEWORK_ID+4);
        args.put("user_id",USER_ID+4);
        args.put("title", TITLE);
        args.put("tstamp", ts);
        args.put("link", LINK);
        args.put("type", TYPE.name());
        jdbcInsert.executeAndReturnKey(args);

        List<Content> contents = contentDao.getContentByFrameworkAndUser(FRAMEWORK_ID,USER_ID);

        assertEquals(false,contents.isEmpty());
        assertEquals(3,contents.size());
        for (Content c:contents) {
            assertEquals(FRAMEWORK_ID, c.getFrameworkId());
            assertEquals(USER_ID,c.getUserId());
        }
    }
    @Test
    public void testGetByFrameworkAndType() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate,"content");
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        for (int i = 0; i < 3; i++) {
            final Map<String, Object> args = new HashMap<>();
            args.put("framework_id", FRAMEWORK_ID);
            args.put("user_id",USER_ID+i);
            args.put("title", TITLE+i);
            args.put("tstamp", ts);
            args.put("link", LINK);
            args.put("type", TYPE.name());
            jdbcInsert.executeAndReturnKey(args);
        }
        final Map<String, Object> args = new HashMap<>();
        args.put("framework_id", FRAMEWORK_ID+4);
        args.put("user_id",USER_ID+4);
        args.put("title", TITLE);
        args.put("tstamp", ts);
        args.put("link", LINK);
        args.put("type", TYPE.name());
        jdbcInsert.executeAndReturnKey(args);

        List<Content> contents = contentDao.getContentByFrameworkAndType(FRAMEWORK_ID,TYPE);

        assertEquals(false,contents.isEmpty());
        assertEquals(3,contents.size());
        for (Content c:contents) {
            assertEquals(FRAMEWORK_ID, c.getFrameworkId());
            assertEquals(TYPE,c.getType());
        }
    }
    @Test
    public void testGetByFrameworkAndTypeAndTitle() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate,"content");
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        for (int i = 0; i < 3; i++) {
            final Map<String, Object> args = new HashMap<>();
            args.put("framework_id", FRAMEWORK_ID);
            args.put("user_id",USER_ID+i);
            args.put("title", TITLE+i);
            args.put("tstamp", ts);
            args.put("link", LINK);
            args.put("type", TYPE.name());
            jdbcInsert.executeAndReturnKey(args);
        }
        final Map<String, Object> args = new HashMap<>();
        args.put("framework_id", FRAMEWORK_ID+4);
        args.put("user_id",USER_ID+4);
        args.put("title", TITLE);
        args.put("tstamp", ts);
        args.put("link", LINK);
        args.put("type", TYPE.name());
        jdbcInsert.executeAndReturnKey(args);

        List<Content> contents = contentDao.getContentByFrameworkAndTypeAndTitle(FRAMEWORK_ID,TYPE,TITLE+0);

        assertEquals(false,contents.isEmpty());
        assertEquals(1,contents.size());
        for (Content c:contents) {
            assertEquals(FRAMEWORK_ID, c.getFrameworkId());
            assertEquals(TYPE,c.getType());
            assertEquals(TITLE+0,c.getTitle());
        }
    }
    @Test
    public void testGetByUser() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate,"content");
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        for (int i = 0; i < 3; i++) {
            final Map<String, Object> args = new HashMap<>();
            args.put("framework_id", FRAMEWORK_ID+i);
            args.put("user_id",USER_ID);
            args.put("title", TITLE);
            args.put("tstamp", ts);
            args.put("link", LINK);
            args.put("type", TYPE.name());
            jdbcInsert.executeAndReturnKey(args);
        }
        final Map<String, Object> args = new HashMap<>();
        args.put("framework_id", FRAMEWORK_ID+4);
        args.put("user_id",USER_ID+4);
        args.put("title", TITLE);
        args.put("tstamp", ts);
        args.put("link", LINK);
        args.put("type", TYPE.name());
        jdbcInsert.executeAndReturnKey(args);

        List<Content> contents = contentDao.getContentByUser(USER_ID);

        assertEquals(false,contents.isEmpty());
        assertEquals(3,contents.size());
        for (Content c:contents) {
            assertEquals(USER_ID,c.getUserId());
        }
    }

    //</editor-fold>
}
