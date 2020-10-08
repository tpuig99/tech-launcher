package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.TestConfig;
import ar.edu.itba.paw.models.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class FrameworkDaoImplTest {

    private static final long FRAMEWORK_ID = 1;
    private static final String FRAMEWORK_NAME = "name";
    private static final long USER_ID = 1;
    private static final FrameworkCategories CATEGORY = Enum.valueOf(FrameworkCategories.class, "Platforms");
    private static final String DESCRIPTION = "description";
    private static final String INTRODUCTION = "introduction";
    private static final String LOGO = "logo";
    private static final FrameworkType TYPE = Enum.valueOf(FrameworkType.class, "Framework");
    private static final long AUTHOR = 1;
    private static final String AUTHOR_NAME = "user1";
    private static final byte[] PICTURE = null;

    @Autowired
    private DataSource ds;

    @Autowired
    private FrameworkDaoImpl frameworkDao;

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("frameworks")
                .usingGeneratedKeyColumns("framework_id");

        JdbcTestUtils.deleteFromTables(jdbcTemplate, "frameworks");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "content");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "users");

        for (int i = 1; i < 6; i++) {
            jdbcTemplate.execute("insert into users values("+i+",'user"+i+"','mail"+i+"',null,default,default,default,default)");
          //  jdbcTemplate.execute("insert into content values("+i+",'user"+i+"','Title','link','book')");
        }
    }

    @Test
    public void testFindById() throws SQLException {
        //Preconditions
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "frameworks");
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        final Map<String, Object> args = new HashMap<>();

        args.put("framework_id", FRAMEWORK_ID);
        args.put("framework_name", FRAMEWORK_NAME);
        args.put("category", CATEGORY.getNameCat());
        args.put("description", DESCRIPTION);
        args.put("introduction", INTRODUCTION);
        args.put("logo", LOGO);
        args.put("type", TYPE.getType());
        args.put("date", ts);
        args.put("author", AUTHOR);

        Number frameworkId = jdbcInsert.executeAndReturnKey(args);

        //Class under test
        Optional<Framework> framework = frameworkDao.findById(frameworkId.longValue());

        //Asserts
        Assert.assertTrue(framework.isPresent());
        Assert.assertEquals(FRAMEWORK_ID, framework.get().getId());
        Assert.assertEquals(FRAMEWORK_NAME, framework.get().getName());
        Assert.assertEquals(CATEGORY.getNameCat(), framework.get().getCategory());
        Assert.assertEquals(DESCRIPTION, framework.get().getDescription());
        Assert.assertEquals(INTRODUCTION, framework.get().getIntroduction());
        Assert.assertEquals(LOGO, framework.get().getLogo());
        Assert.assertEquals(TYPE.getType(), framework.get().getType());
        Assert.assertEquals(AUTHOR_NAME, framework.get().getAuthor());
        //Assert.assertNull(framework.get().getBase64image());
    }

    @Test
    public void tesFindByIdNotExists() {
        //Preconditions
        JdbcTestUtils.deleteFromTables(jdbcTemplate,"frameworks");

        //Class under test
        Optional<Framework> framework = frameworkDao.findById(FRAMEWORK_ID);

        //Asserts
        assertEquals(false,framework.isPresent());
    }

    @Test
    public void testCreate() {
        //Preconditions
        JdbcTestUtils.deleteFromTables(jdbcTemplate,"frameworks");

        //Class under test
        Optional<Framework> framework = frameworkDao.create(FRAMEWORK_NAME,CATEGORY,DESCRIPTION, INTRODUCTION, TYPE,USER_ID,PICTURE);

        //Asserts
        assertNotNull(framework);
        Assert.assertEquals(FRAMEWORK_ID, framework.get().getId());
        Assert.assertEquals(FRAMEWORK_NAME, framework.get().getName());
        Assert.assertEquals(CATEGORY.getNameCat(), framework.get().getCategory());
        Assert.assertEquals(DESCRIPTION, framework.get().getDescription());
        Assert.assertEquals(INTRODUCTION, framework.get().getIntroduction());
        Assert.assertEquals(TYPE.getType(), framework.get().getType());
        Assert.assertEquals(AUTHOR_NAME, framework.get().getAuthor());
        Assert.assertNull(framework.get().getLogo());
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
        args.put("category", CATEGORY.getNameCat());
        args.put("description", DESCRIPTION);
        args.put("introduction", INTRODUCTION);
        args.put("logo", LOGO);
        args.put("type", TYPE.getType());
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
            args.put("category", CATEGORY.getNameCat());
            args.put("description", DESCRIPTION);
            args.put("introduction", INTRODUCTION);
            args.put("logo", LOGO);
            args.put("type", TYPE.getType());
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


    @Test
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



}
