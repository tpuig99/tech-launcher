package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.TestConfig;
import ar.edu.itba.paw.models.Framework;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Sql("classpath:schema.sql")
public class FrameworkDaoImplTest {

    @Autowired
    private DataSource ds;

    @Autowired
    private FrameworkDaoImpl frameworkDao;

    private JdbcTemplate jdbcTemplate;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "frameworks");
    }

    @Test
    @Sql("classpath:framework/insert_framework.sql")
    public void testFindById() throws SQLException {

        long id = 1;

        //Class under test
        Optional<Framework> framework = frameworkDao.findById(id);

        //Asserts
        Assert.assertTrue(framework.isPresent());
        Assert.assertEquals(id, framework.get().getId());
    }
}
