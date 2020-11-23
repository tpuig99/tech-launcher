package ar.edu.itba.paw;


import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.persistence.PostDao;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static junit.framework.Assert.*;

@Rollback
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class PostDaoTest {

    private long USER_ID = 1;
    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";

    @Autowired
    private DataSource ds;

    @Autowired
    private PostDao postDao;

    @PersistenceContext
    private EntityManager em;

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("posts")
                .usingGeneratedKeyColumns("post_id");

        JdbcTestUtils.deleteFromTables(jdbcTemplate, "users");

        for (int i = 1; i < 6; i++) {
            User user = new User("user"+i,"mail"+i,null,true,"",true,null);
            em.persist(user);

            em.flush();
            USER_ID = user.getId();
        }

    }

    @Test
    public void testCreate() {
        JdbcTestUtils.deleteFromTableWhere(jdbcTemplate,"posts","title = "+TITLE);

        final Post post = postDao.insertPost(USER_ID, TITLE, DESCRIPTION);

        em.flush();
        assertNotNull(post);
        assertEquals(USER_ID, post.getUser().getId().longValue());
        assertEquals(TITLE,post.getTitle());
        assertEquals(DESCRIPTION, post.getDescription());
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "posts","post_id ="+post.getPostId()));
    }

    @Test(expected = Exception.class)
    public void testCreateOnExisting() {
        
        JdbcTestUtils.deleteFromTableWhere(jdbcTemplate,"posts","title = ",TITLE);
        
        final Map<String, Object> args = new HashMap<>();
        args.put("description", DESCRIPTION);
        args.put("user_id",USER_ID);
        args.put("title", TITLE);
        jdbcInsert.execute(args);

       final Post post = postDao.insertPost(USER_ID, TITLE, DESCRIPTION);
    }

    @Test
    public void testDelete() {
        JdbcTestUtils.deleteFromTableWhere(jdbcTemplate,"posts","title = "+TITLE);
        Timestamp ts = new Timestamp(System.currentTimeMillis());

        Post post  = new Post();
        post.setUser(em.find(User.class,USER_ID));
        post.setTitle(TITLE);
        post.setDescription(DESCRIPTION);
        post.setTimestamp(ts);
        em.persist(post);

        postDao.deletePost(post.getPostId());

        em.flush();
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "posts","post_id ="+post.getPostId()));
    }

    @Test
    public void testGetById() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate,"posts");
        Timestamp ts = new Timestamp(System.currentTimeMillis());

        Post post  = new Post();
        post.setUser(em.find(User.class,USER_ID));
        post.setTitle(TITLE);
        post.setDescription(DESCRIPTION);
        post.setTimestamp(ts);
        em.persist(post);

        Optional<Post> post2 = postDao.findById(post.getPostId());

        em.flush();
        assertTrue(post2.isPresent());
        assertEquals(USER_ID,post2.get().getUser().getId().longValue());
        assertEquals(TITLE, post2.get().getTitle());
        assertEquals(DESCRIPTION, post2.get().getDescription());
    }

    @Test
    public void testGetByUser() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate,"posts");
        Timestamp ts = new Timestamp(System.currentTimeMillis());

        for (int i = 0; i < 4;i++) {
            Post post  = new Post();
            post.setUser(em.find(User.class,USER_ID));
            post.setTitle(TITLE);
            post.setDescription(DESCRIPTION);
            post.setTimestamp(ts);
            em.persist(post);
        }

        Post post  = new Post();
        post.setUser(em.find(User.class,USER_ID-1));
        post.setTitle(TITLE);
        post.setDescription(DESCRIPTION);
        post.setTimestamp(ts);
        em.persist(post);

        List<Post> posts = postDao.getPostsByUser(USER_ID,1,5);

        em.flush();
        assertFalse(posts.isEmpty());
        assertEquals(4, posts.size());
        for (Post p : posts) {
            assertEquals(USER_ID, p.getUser().getId().longValue());
        }
    }



}
