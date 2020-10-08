package ar.edu.itba.paw;

import ar.edu.itba.paw.models.Comment;
import ar.edu.itba.paw.persistence.CommentDao;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.util.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class CommentDaoTest {

    @Autowired
    private DataSource ds;

    @Autowired
    private CommentDao commentDao;

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;
    private final long FRAMEWORK_ID = 1;
    private final long USER_ID = 1;
    private final long INVALID_ID=99;
    private final long SIZE = 5;
    private final String DESCRIPTION = "Un Anillo para gobernarlos a todos. Un Anillo para encontrarlos, un Anillo para atraerlos a todos y atarlos en las tinieblas";
    private final String DESCRIPTION_NEW = "Ash Nazg durbatulûk, ash Nazg gimbatul, ash Nazg thrakatulûk agh burzum-ishi krimpatul.";

    private Map<String, Object> getArgumentsMap(long frameworkId, long userId, String description, Timestamp timestamp, Long reference) {
        final Map<String, Object> args = new HashMap<>();
        args.put("framework_id", frameworkId);
        args.put("user_id", userId);
        args.put("description", description);
        args.put("tstamp", timestamp);
        args.put("reference", reference);
        return args;
    }

    @Before
    public void setUp() {
        this.jdbcTemplate = new JdbcTemplate(ds);
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("comments")
                .usingGeneratedKeyColumns("comment_id");

        JdbcTestUtils.deleteFromTables(jdbcTemplate, "comments");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "users");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "frameworks");

        for (int i = 1; i < SIZE+1; i++) {
            jdbcTemplate.execute("insert into users values("+i+",'user"+i+"','mail"+i+"',null,default,default,default,default)");
            jdbcTemplate.execute("insert into frameworks values("+i+",'framework"+i+"','Media','description','introduction',default,'Framework',default,default,default )");
        }
    }

    @Test
    public void testInsertComment() {
        // Arrange delegated

        // Act
        final Comment comment = commentDao.insertComment(FRAMEWORK_ID, USER_ID, DESCRIPTION, null);

        // Assert
        Assert.assertEquals(FRAMEWORK_ID, comment.getFrameworkId());
        Assert.assertEquals(USER_ID, comment.getUserId());
        Assert.assertEquals(DESCRIPTION, comment.getDescription());
        Assert.assertEquals(0, comment.getReference().intValue());
        Assert.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "comments", "comment_id =" + comment.getCommentId()));
    }

    @Test
    public void testReplyComment() {
        // Arrange
        final Map<String, Object> args = getArgumentsMap(FRAMEWORK_ID, USER_ID, DESCRIPTION, new Timestamp(System.currentTimeMillis()), null);
        final int commentId = jdbcInsert.executeAndReturnKey(args).intValue();

        // Act
        final Comment comment = commentDao.insertComment(FRAMEWORK_ID, USER_ID, DESCRIPTION, (long) commentId);

        // Assert
        Assert.assertNotNull(comment.getReference());
        Assert.assertEquals(commentId, comment.getReference().intValue());
        Assert.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "comments", "reference =" + comment.getReference()));
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void testInsertWithInvalidUser() {
        // Arrange delegated

        // Act
        commentDao.insertComment(FRAMEWORK_ID, INVALID_ID, DESCRIPTION, null);

        // Assert on Exception
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void testInsertWithInvalidFramework() {
        // Arrange delegated

        // Act
        commentDao.insertComment(INVALID_ID, USER_ID, DESCRIPTION, null);

        // Assert on Exception
    }

    @Test
    public void testDeleteComment() {
        // Arrange
        final Map<String, Object> args = getArgumentsMap(FRAMEWORK_ID, USER_ID, DESCRIPTION, new Timestamp(System.currentTimeMillis()), null);
        final int commentId = jdbcInsert.executeAndReturnKey(args).intValue();

        // Act
        final int returnValue = commentDao.deleteComment(commentId);

        // Assert
        Assert.assertEquals(1, returnValue);
        Assert.assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "comments", "comment_id =" + returnValue));
    }

    @Test
    public void testDeleteCommentWithResponses() {
        // Arrange
        Map<String, Object> args = getArgumentsMap(FRAMEWORK_ID, USER_ID, Math.random() > 0.5 ? DESCRIPTION : DESCRIPTION_NEW, new Timestamp(System.currentTimeMillis()), null);
        long commentId = jdbcInsert.executeAndReturnKey(args).longValue();
        for (int i = 1; i < SIZE; i++) {
            args = getArgumentsMap(FRAMEWORK_ID, USER_ID+i, Math.random() > 0.5 ? DESCRIPTION : DESCRIPTION_NEW, new Timestamp(System.currentTimeMillis()), commentId);
            jdbcInsert.executeAndReturnKey(args);
        }

        // Act
        final int returnValue = commentDao.deleteComment(commentId);

        // Assert
        Assert.assertEquals(SIZE, returnValue);
        Assert.assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "comments", "comment_id =" + commentId));
        Assert.assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "comments", "reference =" + commentId));
    }

    @Test
    public void testChangeComment() {
        // Arrange
        final Map<String, Object> args = getArgumentsMap(FRAMEWORK_ID, USER_ID, DESCRIPTION, new Timestamp(System.currentTimeMillis()), null);
        final int commentId = jdbcInsert.executeAndReturnKey(args).intValue();

        // Act
        final Optional<Comment> returnValue = commentDao.changeComment(commentId, DESCRIPTION_NEW);

        // Assert
        Assert.assertTrue(returnValue.isPresent());
        Assert.assertNotEquals(DESCRIPTION, returnValue.get().getDescription());
        Assert.assertEquals(DESCRIPTION_NEW, returnValue.get().getDescription());
        Assert.assertEquals(commentId, returnValue.get().getCommentId());
    }

    @Test
    public void testGetCommentsByFramework() {
        // Arrange
        for (int i = 0; i < SIZE; i++) {
            final Map<String, Object> args = getArgumentsMap(FRAMEWORK_ID, USER_ID+i, Math.random() > 0.5 ? DESCRIPTION : DESCRIPTION_NEW, new Timestamp(System.currentTimeMillis()), null);
            jdbcInsert.executeAndReturnKey(args);
        }

        // Act
        final List<Comment> commentList = commentDao.getCommentsByFramework(FRAMEWORK_ID, null);

        // Assert
        Assert.assertFalse(commentList.isEmpty());
        Assert.assertEquals(SIZE, commentList.size());
    }

    @Test
    public void testGetCommentsByUser() {
        // Arrange
        for (int i = 0; i < SIZE; i++) {
            final Map<String, Object> args = getArgumentsMap(FRAMEWORK_ID+i, USER_ID, Math.random() > 0.5 ? DESCRIPTION : DESCRIPTION_NEW, new Timestamp(System.currentTimeMillis()), null);
            jdbcInsert.executeAndReturnKey(args);
        }

        // Act
        final List<Comment> commentList = commentDao.getCommentsByUser(USER_ID, 1, 5);

        // Assert
        Assert.assertFalse(commentList.isEmpty());
        Assert.assertEquals(SIZE, commentList.size());
    }

    @Test
    public void getCommentsByFrameworkAndUser() {
        // Arrange
        for (int i = 0; i < SIZE; i++) {
            final Map<String, Object> args = getArgumentsMap(FRAMEWORK_ID+i, USER_ID+i, Math.random() > 0.5 ? DESCRIPTION : DESCRIPTION_NEW, new Timestamp(System.currentTimeMillis()), null);
            jdbcInsert.executeAndReturnKey(args);
        }

        // Act
        final List<Comment> returnValue = commentDao.getCommentsByFrameworkAndUser(FRAMEWORK_ID, USER_ID);

        // Assert
        Assert.assertFalse(returnValue.isEmpty());
        Assert.assertEquals(1, returnValue.size());
    }

    @Test
    public void testGetRepliesByFramework() {
        // Arrange
        Map<String, Object> args = getArgumentsMap(FRAMEWORK_ID, USER_ID, Math.random() > 0.5 ? DESCRIPTION : DESCRIPTION_NEW, new Timestamp(System.currentTimeMillis()), null);
        long commentId = jdbcInsert.executeAndReturnKey(args).longValue();
        for (int i = 1; i < SIZE; i++) {
            args = getArgumentsMap(FRAMEWORK_ID, USER_ID+i, Math.random() > 0.5 ? DESCRIPTION : DESCRIPTION_NEW, new Timestamp(System.currentTimeMillis()), commentId);
            jdbcInsert.executeAndReturnKey(args);
        }

        // Act
        final Map<Long, List<Comment>> returnValue = commentDao.getRepliesByFramework(FRAMEWORK_ID);

        // Assert
        Assert.assertFalse(returnValue.isEmpty());
        Assert.assertEquals(1, returnValue.size());
        Assert.assertEquals(SIZE-1, returnValue.get(commentId).size());
    }

    @Test
    public void testGetCommentsWithoutReferenceByFrameworkWithUser() {
        // Arrange
        Map<String, Object> args = getArgumentsMap(FRAMEWORK_ID, USER_ID, Math.random() > 0.5 ? DESCRIPTION : DESCRIPTION_NEW, new Timestamp(System.currentTimeMillis()), null);
        long commentId = jdbcInsert.executeAndReturnKey(args).longValue();
        for (int i = 1; i < SIZE; i++) {
            args = getArgumentsMap(FRAMEWORK_ID, USER_ID+i, Math.random() > 0.5 ? DESCRIPTION : DESCRIPTION_NEW, new Timestamp(System.currentTimeMillis()), commentId);
            jdbcInsert.executeAndReturnKey(args);
        }

        // Act
        final List<Comment> returnValue = commentDao.getCommentsWithoutReferenceByFrameworkWithUser(FRAMEWORK_ID, null, 1, 5);

        // Assert
        Assert.assertFalse(returnValue.isEmpty());
        Assert.assertEquals(1, returnValue.size());
    }
}
