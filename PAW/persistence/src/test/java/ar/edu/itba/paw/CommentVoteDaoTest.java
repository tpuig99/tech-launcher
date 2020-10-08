package ar.edu.itba.paw;

import ar.edu.itba.paw.models.CommentVote;
import ar.edu.itba.paw.persistence.CommentVoteDao;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static junit.framework.Assert.assertEquals;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class CommentVoteDaoTest {

    @Autowired
    private DataSource ds;

    @Autowired
    private CommentVoteDao commentVoteDao;

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;

    private final long USER_ID = 1;
    private final long INVALID_ID=99;
    private final long SIZE = 5;
    private final long COMMENT_ID=1;
    private final int VOTE_UP=1;

    private Map<String, Object> getArgumentsMap(long commentId, long userId, int vote) {
        final Map<String, Object> args = new HashMap<>();
        args.put("comment_id", commentId); // la key es el nombre de la columna
        args.put("user_id",userId);
        args.put("vote",vote);
        return args;
    }

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("comment_votes")
                .usingGeneratedKeyColumns("vote_id");

        JdbcTestUtils.deleteFromTables(jdbcTemplate, "comment_votes");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "comments");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "users");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "frameworks");
        Timestamp ts = new Timestamp(System.currentTimeMillis());

        for (int i = 1; i < 6; i++) {
            jdbcTemplate.execute("insert into users values("+i+",'user"+i+"','mail"+i+"',null,default,default,default,default)");
            jdbcTemplate.execute("insert into frameworks values("+i+",'framework"+i+"','Media','description','introduction',default,'Framework',default,default,default )");
            String DESCRIPTION = "Un Anillo para gobernarlos a todos. Un Anillo para encontrarlos, un Anillo para atraerlos a todos y atarlos en las tinieblas";
            jdbcTemplate.execute("insert into comments values("+i+","+i+","+i+",'"+ DESCRIPTION +i+"','"+ts+"',null)");
        }
    }

    @Test
    public void testInsertVote() {
        // Arrange delegated

        // Act
        final CommentVote returnValue = commentVoteDao.insert(COMMENT_ID,USER_ID, VOTE_UP);

        // Assert
        Assert.assertNotNull(returnValue);
        Assert.assertEquals(COMMENT_ID, returnValue.getCommentId());
        Assert.assertEquals(USER_ID, returnValue.getUserId());
        Assert.assertTrue(returnValue.isVoteUp());
        Assert.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "comment_votes","vote_id ="+returnValue.getCommentVoteId()));
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void testInsertVoteInvalidUser() {
        // Arrange delegated

        // Act
        commentVoteDao.insert(COMMENT_ID,INVALID_ID, VOTE_UP);

        // Assert on Exception
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void testInsertVoteInvalidComment() {
        // Arrange delegated

        // Act
        commentVoteDao.insert(INVALID_ID,USER_ID, VOTE_UP);

        // Assert on Exception
    }

    @Test
    public void testChangeVote() {
        // Arrange
        final Map<String, Object> args = getArgumentsMap(COMMENT_ID, USER_ID, VOTE_UP);
        final int voteId = jdbcInsert.executeAndReturnKey(args).intValue();

        // Act
        int VOTE_DOWN = -1;
        final Optional<CommentVote> returnValue = commentVoteDao.update(voteId, VOTE_DOWN);

        // Assert
        Assert.assertTrue(returnValue.isPresent());
        Assert.assertEquals(voteId, returnValue.get().getCommentVoteId());
        Assert.assertFalse(returnValue.get().isVoteUp());
    }

    @Test
    public void testDeleteVote() {
        // Arrange
        final Map<String, Object> args = getArgumentsMap(COMMENT_ID, USER_ID, VOTE_UP);
        final int voteId = jdbcInsert.executeAndReturnKey(args).intValue();

        // Act
        final int returnValue = commentVoteDao.delete(voteId);

        // Asserts
        assertEquals(1, returnValue);
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "comment_votes","vote_id ="+ returnValue));
    }


    @Test
    public void testGetByComment() {
        // Arrange
        for (int i = 0; i < SIZE; i++) {
            final Map<String,Object> args = getArgumentsMap(COMMENT_ID, USER_ID+i, VOTE_UP);
            jdbcInsert.executeAndReturnKey(args);
        }

        // Act
        final List<CommentVote> returnValue = commentVoteDao.getByComment(COMMENT_ID);

        // Assert
        Assert.assertFalse(returnValue.isEmpty());
        Assert.assertEquals(SIZE, returnValue.size());
    }

    @Test
    public void testGetByCommentAndUser() {
        // Arrange
        for (int i = 0; i < SIZE; i++) {
            final Map<String,Object> args = getArgumentsMap(COMMENT_ID+i, USER_ID+i, VOTE_UP);
            jdbcInsert.executeAndReturnKey(args);
        }

        // Act
        final Optional<CommentVote> returnValue = commentVoteDao.getByCommentAndUser(COMMENT_ID, USER_ID);

        // Assert
        Assert.assertTrue(returnValue.isPresent());
        Assert.assertEquals(COMMENT_ID, returnValue.get().getCommentId());
        Assert.assertEquals(USER_ID, returnValue.get().getUserId());
    }

}
