package ar.edu.itba.paw;

import ar.edu.itba.paw.models.ContentTypes;
import ar.edu.itba.paw.models.ReportContent;
import ar.edu.itba.paw.persistence.ReportContentDao;
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
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static junit.framework.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class ReportContentDaoTest {
    private static final long CONTENT_ID = 1;
    private static final long FRAMEWORK_ID = 1;
    private static final long USER_ID = 1;
    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";
    private static final String LINK = "link";
    private static final ContentTypes TYPE = Enum.valueOf(ContentTypes.class,"book");

    @Autowired
    private DataSource ds;
    @Autowired
    private ReportContentDao reportContentDao;

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("content_report")
                .usingGeneratedKeyColumns("report_id");

        JdbcTestUtils.deleteFromTables(jdbcTemplate, "content_report");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "content");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "users");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "frameworks");
        Timestamp ts = new Timestamp(System.currentTimeMillis());

        for (int i = 1; i < 7; i++) {
            jdbcTemplate.execute("insert into users values("+i+",'user"+i+"','mail"+i+"',null,default,default,default,default)");
            jdbcTemplate.execute("insert into frameworks values("+i+",'framework"+i+"','Media','description','introduction',default,'Framework',default,default,default )");
            jdbcTemplate.execute("insert into content values("+i+","+i+","+i+",'"+TITLE+i+" ','"+ts+"','"+LINK+"','"+TYPE.name()+"')");
        }
    }

    //<editor-fold desc="reportContent methods">
    @Test
    public void testCreate() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate,"content_report");
        reportContentDao.add(CONTENT_ID,USER_ID,DESCRIPTION);

        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "content_report"));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "content_report","content_id="+CONTENT_ID+" and user_id="+USER_ID));
    }
    @Test(expected = Exception.class)
    public void testCreateOnExisting() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate,"content_report");
        final Map<String, Object> args = new HashMap<>();
        args.put("content_id", CONTENT_ID);
        args.put("user_id",USER_ID);
        args.put("description", DESCRIPTION);
        jdbcInsert.execute(args);

        reportContentDao.add(CONTENT_ID,USER_ID,DESCRIPTION);
    }

    @Test(expected = Exception.class)
    public void testCreateWithoutUser() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate,"content_report");
        reportContentDao.add(CONTENT_ID,USER_ID+7,DESCRIPTION);
    }
    @Test(expected = Exception.class)
    public void testCreateWithoutContent() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate,"content_report");
        reportContentDao.add(CONTENT_ID+7,USER_ID,DESCRIPTION);
    }

    @Test
    public void testDelete() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate,"content_report");
        final Map<String, Object> args = new HashMap<>();
        args.put("content_id", CONTENT_ID);
        args.put("user_id",USER_ID);
        args.put("description", DESCRIPTION);
        Number id = jdbcInsert.executeAndReturnKey(args);


        reportContentDao.delete(id.longValue());
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "content_report","report_id ="+id.longValue()));
    }
    @Test
    public void testDeleteByContent() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate,"content_report");
        for (int i = 0; i < 2; i++) {
            final Map<String, Object> args = new HashMap<>();
            args.put("content_id", CONTENT_ID);
            args.put("user_id",USER_ID+i);
            args.put("description", DESCRIPTION);
            jdbcInsert.executeAndReturnKey(args);
        }

        reportContentDao.deleteByContent(CONTENT_ID);
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "content_report","content_id ="+CONTENT_ID));
    }
    //</editor-fold>

    //<editor-fold desc="Getters">
    @Test
    public void testGetById() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate,"content_report");
        final Map<String, Object> args = new HashMap<>();
        args.put("content_id", CONTENT_ID);
        args.put("user_id",USER_ID);
        args.put("description", DESCRIPTION);
        Number id = jdbcInsert.executeAndReturnKey(args);

        final Optional<ReportContent> content = reportContentDao.getById(id.longValue());

        assertEquals(true,content.isPresent());
        assertEquals(CONTENT_ID, content.get().getContentId());
        assertEquals(USER_ID,content.get().getUserId());
        assertEquals(DESCRIPTION, content.get().getReportDescription());
        assertEquals(FRAMEWORK_ID,content.get().getFrameworkId());
        assertEquals(String.valueOf("framework"+FRAMEWORK_ID),content.get().getFrameworkName());
    }
    @Test
    public void testGetByIdEmpty() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate,"content_report");

        final Optional<ReportContent> content = reportContentDao.getById(1);
        assertEquals(false,content.isPresent());
    }

    @Test
    public void testGetAll() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate,"content_report");
        for (int i = 0; i < 5; i++) {
            final Map<String, Object> args = new HashMap<>();
            args.put("content_id", CONTENT_ID);
            args.put("user_id",USER_ID+i);
            args.put("description", DESCRIPTION);
            jdbcInsert.execute(args);
            args.clear();
            args.put("content_id", CONTENT_ID+i+1);
            args.put("user_id",USER_ID);
            args.put("description", DESCRIPTION);
            jdbcInsert.execute(args);
        }

        final List<ReportContent> content = reportContentDao.getAll(1, 10);

        assertEquals(false,content.isEmpty());
        assertEquals(6,content.size());
    }

    @Test
    public void testGetByContent() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate,"content_report");
        for (int i = 0; i < 5; i++) {
            final Map<String, Object> args = new HashMap<>();
            args.put("content_id", CONTENT_ID);
            args.put("user_id",USER_ID+i);
            args.put("description", DESCRIPTION);
            jdbcInsert.execute(args);
            args.clear();
            args.put("content_id", CONTENT_ID+i+1);
            args.put("user_id",USER_ID);
            args.put("description", DESCRIPTION);
            jdbcInsert.execute(args);
        }

        final Optional<ReportContent> content = reportContentDao.getByContent(CONTENT_ID);

        assertEquals(true,content.isPresent());
        assertEquals(5,content.get().getContent().getReportsIds().size());
        assertEquals(CONTENT_ID,content.get().getContentId());
    }

    @Test
    public void testGetByFramework() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate,"content_report");
        for (int i = 0; i < 5; i++) {
            final Map<String, Object> args = new HashMap<>();
            args.put("content_id", CONTENT_ID);
            args.put("user_id",USER_ID+i);
            args.put("description", DESCRIPTION);
            jdbcInsert.execute(args);
            args.clear();
            args.put("content_id", CONTENT_ID+i+1);
            args.put("user_id",USER_ID);
            args.put("description", DESCRIPTION);
            jdbcInsert.execute(args);
        }

        final List<ReportContent> content = reportContentDao.getByFramework(FRAMEWORK_ID);

        assertEquals(false,content.isEmpty());
        assertEquals(1,content.size());
        for (ReportContent r: content) {
            assertEquals(FRAMEWORK_ID,r.getFrameworkId());
            assertEquals(5,r.getContent().getReportersNames().size());
        }
    }
    //</editor-fold>
}
