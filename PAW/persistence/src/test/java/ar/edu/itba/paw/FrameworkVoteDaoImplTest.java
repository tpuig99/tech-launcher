package ar.edu.itba.paw;

import ar.edu.itba.paw.models.FrameworkVote;
import org.junit.Assert;
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

import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class FrameworkVoteDaoImplTest {

    private static final long VOTE_ID = 1L;
    private static final long FRAMEWORK_ID = 1L;
    private static final long USER_ID = 1L;
    private static final int STARS = 0;

    @Autowired
    private DataSource ds;

    @Autowired
    private FrameworkVoteDaoImpl frameworkVoteDao;

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("framework_votes")
                .usingGeneratedKeyColumns("vote_id");

        JdbcTestUtils.deleteFromTables(jdbcTemplate, "frameworks");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "framework_votes");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "users");

        for (int i = 1; i < 4; i++) {
            jdbcTemplate.execute("insert into users values("+i+",'user"+i+"','mail"+i+"',null,default,default,default,default)");
            jdbcTemplate.execute("insert into frameworks values("+i+",'framework"+i+"','Media','description','introduction',default,'Framework',default,default,default )");

        }
    }

    @Test
    public void testCreate() {
        //Preconditions
        JdbcTestUtils.deleteFromTables(jdbcTemplate,"framework_votes");

        //Class under test
        final FrameworkVote frameworkVote = frameworkVoteDao.insert(FRAMEWORK_ID,USER_ID,STARS);

        //Asserts
        assertNotNull(frameworkVote);
        Assert.assertEquals(VOTE_ID, frameworkVote.getVoteId());
        Assert.assertEquals(FRAMEWORK_ID, frameworkVote.getFrameworkId());
        Assert.assertEquals(USER_ID, frameworkVote.getUserId());
        Assert.assertEquals(STARS, frameworkVote.getStars());

        Assert.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "framework_votes","framework_id ="+frameworkVote.getVoteId()));
    }


}


