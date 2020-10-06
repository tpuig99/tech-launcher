package ar.edu.itba.paw;

import ar.edu.itba.paw.models.Content;
import ar.edu.itba.paw.models.ContentTypes;
import ar.edu.itba.paw.persistence.ContentDao;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;

import javax.sql.DataSource;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Sql("classpath:schema.sql")
public class ContentDaoTest {
    private static final long FRAMEWORK_ID = 1;
    private static final long USER_ID = 1;
    private static final String TITLE = "title";
    private static final String LINK = "link";
    private static final ContentTypes TYPE = Enum.valueOf(ContentTypes.class,"book");

    @Autowired
    private DataSource ds;
    @Autowired
    private ContentDao contentDao;
    private JdbcTemplate jdbcTemplate;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "content");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "users");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "frameworks");
        
    }
    @Test
    public void testCreate() {
        final Content content = contentDao.insertContent(FRAMEWORK_ID,USER_ID,TITLE,LINK,TYPE);

        assertNotNull(content);
        assertEquals(FRAMEWORK_ID, content.getFrameworkId());
        assertEquals(USER_ID,content.getUserId());
        assertEquals(TITLE, content.getTitle());
        assertEquals(TYPE,content.getType());
        assertEquals(LINK,content.getLink());
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "content"));
    }
}
