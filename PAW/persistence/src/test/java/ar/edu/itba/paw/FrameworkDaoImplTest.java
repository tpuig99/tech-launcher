package ar.edu.itba.paw;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.persistence.ContentDao;
import ar.edu.itba.paw.persistence.FrameworkHibernateDaoImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;


@Rollback
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class FrameworkDaoImplTest {

    private static final long FRAMEWORK_ID = 1;
    private static final String FRAMEWORK_NAME = "name";
    private static final FrameworkCategories CATEGORY = Enum.valueOf(FrameworkCategories.class, "Platforms");
    private static final String DESCRIPTION = "description";
    private static final String INTRODUCTION = "introduction";
    private static final FrameworkType TYPE = Enum.valueOf(FrameworkType.class, "Framework");
    private static final long AUTHOR = 1;
    private static final String AUTHOR_NAME = "user1";
    private static final byte[] PICTURE = null;

    private static long USER_ID = 1;


    @Autowired
    private DataSource ds;

    @Autowired
    private FrameworkHibernateDaoImpl frameworkDao;

    @PersistenceContext
    private EntityManager em;

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("frameworks")
                .usingGeneratedKeyColumns("framework_id");

        JdbcTestUtils.deleteFromTables(jdbcTemplate, "frameworks");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "content");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "users");

        for (int i = 1; i < 6; i++) {
            User user = new User("user"+i,"mail"+i,null,true,"",true,null);
            em.persist(user);
            em.flush();
            USER_ID = user.getId();
        }
    }

    @Test
    public void testFindById() throws SQLException {
        //Preconditions
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "frameworks");

        Date date = new Date(System.currentTimeMillis());

        Framework framework = new Framework();
        framework.setName(FRAMEWORK_NAME);
        framework.setCategory(CATEGORY);
        framework.setType(TYPE);
        framework.setDescription(DESCRIPTION);
        framework.setIntroduction(INTRODUCTION);
        framework.setAuthor(em.find(User.class, USER_ID));
        framework.setPublishDate(date);
        em.persist(framework);

        //Class under test
        Optional<Framework> framework2 = frameworkDao.findById(framework.getId());
        em.flush();

        //Asserts
        Assert.assertTrue(framework2.isPresent());
        Assert.assertEquals(FRAMEWORK_NAME, framework2.get().getName());
        Assert.assertEquals(CATEGORY, framework2.get().getCategory());
        Assert.assertEquals(DESCRIPTION, framework2.get().getDescription());
        Assert.assertEquals(INTRODUCTION, framework2.get().getIntroduction());
        Assert.assertEquals(TYPE, framework2.get().getType());
        Assert.assertEquals(USER_ID, framework2.get().getAuthor().getId().longValue());
        Assert.assertEquals(date, framework2.get().getPublishDate());

    }

   /* @Test
    public void tesFindByIdNotExists() {
        //Preconditions
        JdbcTestUtils.deleteFromTables(jdbcTemplate,"frameworks");

        //Class under test
        Optional<Framework> framework = frameworkDao.findById(FRAMEWORK_ID);
        em.flush();

        //Asserts
        assertEquals(false,framework.isPresent());
    }

    @Test
    public void testCreate() {
        //Preconditions
        JdbcTestUtils.deleteFromTables(jdbcTemplate,"frameworks");

        //Class under test
        final Optional<Framework> framework = frameworkDao.create(FRAMEWORK_NAME,CATEGORY,DESCRIPTION,INTRODUCTION,TYPE, USER_ID,PICTURE);
        em.flush();

        //Asserts
        assertNotNull(framework);
        Assert.assertEquals(FRAMEWORK_ID, framework.get().getId());
        Assert.assertEquals(FRAMEWORK_NAME, framework.get().getName());
        Assert.assertEquals(CATEGORY.name(), framework.get().getCategory());
        Assert.assertEquals(DESCRIPTION, framework.get().getDescription());
        Assert.assertEquals(INTRODUCTION, framework.get().getIntroduction());
        Assert.assertEquals(TYPE.name(), framework.get().getType());
        Assert.assertEquals(AUTHOR_NAME, framework.get().getAuthor());
        Assert.assertNull(framework.get().getPicture());
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "frameworks","framework_id ="+framework.get().getId()));
    }

    @Test(expected = Exception.class)
    public void testCreateAlreadyExists() {
        //Preconditions
        JdbcTestUtils.deleteFromTables(jdbcTemplate,"frameworks");

        Timestamp ts = new Timestamp(System.currentTimeMillis());
        final Map<String, Object> args = new HashMap<>();

        args.put("framework_id", FRAMEWORK_ID);
        args.put("framework_name", FRAMEWORK_NAME);
        args.put("category", CATEGORY.name());
        args.put("description", DESCRIPTION);
        args.put("introduction", INTRODUCTION);
        args.put("type", TYPE.name());
        args.put("date", ts);
        args.put("author", AUTHOR);

        jdbcInsert.execute(args);

        //Class under test
        Optional<Framework> framework = frameworkDao.create(FRAMEWORK_NAME,CATEGORY,DESCRIPTION, INTRODUCTION, TYPE,USER_ID,PICTURE);
    }


    @Test
    public void testGetFrameworkNames(){
        //Preconditions
        JdbcTestUtils.deleteFromTables(jdbcTemplate,"frameworks");

        Timestamp ts = new Timestamp(System.currentTimeMillis());
        List<String> names = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            final Map<String, Object> args = new HashMap<>();
            args.put("framework_id", i);
            args.put("framework_name", FRAMEWORK_NAME+String.valueOf(i));
            args.put("category", CATEGORY.name());
            args.put("description", DESCRIPTION);
            args.put("introduction", INTRODUCTION);
            args.put("type", TYPE.name());
            args.put("date", ts);
            args.put("author", AUTHOR);

            jdbcInsert.execute(args);

            names.add(FRAMEWORK_NAME+String.valueOf(i));
        }

        //Class under test
        List<String> frameworkNames = frameworkDao.getFrameworkNames();

        //Asserts
        Assert.assertFalse(frameworkNames.isEmpty());
        Assert.assertEquals(names, frameworkNames);

    }


   /* @Test
    public void testSearchOnlyByName(){
        //Preconditions
        JdbcTestUtils.deleteFromTables(jdbcTemplate,"frameworks");

        Timestamp ts = new Timestamp(System.currentTimeMillis());

        List<Framework> results = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            final Map<String, Object> args = new HashMap<>();
            args.put("framework_id", i);

            args.put("introduction", INTRODUCTION);
            args.put("logo", LOGO);
            args.put("type", TYPE.getType());
            args.put("date", ts);
            args.put("author", AUTHOR);

            switch(i) {
                case 0:
                    args.put("framework_name", "html");
                    args.put("description", "html is great");
                    break;
                case 1:
                    args.put("framework_name", "canvas");
                    args.put("description", "canvas works with html");
                    //framework = new Framework(i, "canvas", CATEGORY, "canvas works with html", INTRODUCTION, LOGO,0,0,TYPE,AUTHOR_NAME,ts,null,0,"book",null);
                    break;
                case 2:
                    args.put("framework_name", "css");
                    args.put("description", "cascade style sheet");
                    //framework = new Framework(i, "css", CATEGORY, "cascade style sheet", INTRODUCTION, LOGO,0,0,TYPE,AUTHOR_NAME,ts,null,0,"book",null);
                    break;
                case 3:
                    args.put("framework_name", "vue");
                    args.put("description", "use vue with css and html");
                    //framework = new Framework(i, "vue", CATEGORY, "use vue with css and html", INTRODUCTION, LOGO,0,0,TYPE,AUTHOR_NAME,ts,null,0,"book",null);
                    break;
            }

            args.put("category", CATEGORY.getNameCat());

            jdbcInsert.execute(args);
        }
        Framework framework = new Framework(0L,"html",CATEGORY,"html is great", INTRODUCTION, LOGO,0,0,TYPE, AUTHOR_NAME,ts,ts,0,"book",null);

        results.add(framework);
        //Class under test
        List<Framework> matchingFrameworks = frameworkDao.search("html",null, null, null,true);

        //Asserts
        Assert.assertFalse(matchingFrameworks.isEmpty());
        Assert.assertEquals(results, matchingFrameworks);
    }
*/


}
