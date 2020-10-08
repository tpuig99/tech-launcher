package ar.edu.itba.paw;

import ar.edu.itba.paw.models.CommentVote;
import ar.edu.itba.paw.persistence.CommentDao;
import ar.edu.itba.paw.persistence.CommentVoteDao;
import ar.edu.itba.paw.services.CommentServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class CommentServiceTest {
    private final long USER_ID=1;
    private final long COMMENT_ID=1;
    private final int VOTE_UP=1;
    private final int VOTE_DOWN=-1;
    private final long COMMENT_VOTE_MOCK_ID=1;

    @InjectMocks
    private final CommentServiceImpl commentServiceImplMock = new CommentServiceImpl();

    @Mock
    private CommentVoteDao commentVoteDao;

    // Used by commentVoteDao
    @Mock
    private CommentDao commentDao;

    @Test
    public void testVoteUp() {
        // Arrange
        Mockito.when(commentVoteDao.getByCommentAndUser(COMMENT_ID, USER_ID)).thenReturn(Optional.empty());

        // Act
        commentServiceImplMock.voteUp(COMMENT_ID, USER_ID);

        // Assert
        Mockito.verify(commentVoteDao,Mockito.times(1)).insert(COMMENT_ID,USER_ID,VOTE_UP);
        Mockito.verify(commentVoteDao,Mockito.never()).delete(Mockito.anyInt());
        Mockito.verify(commentVoteDao,Mockito.never()).update(Mockito.anyInt(),Mockito.anyInt());
    }

    @Test
    public void testVoteDown() {
        // Arrange
        Mockito.when(commentVoteDao.getByCommentAndUser(COMMENT_ID, USER_ID)).thenReturn(Optional.empty());

        // Act
        commentServiceImplMock.voteDown(COMMENT_ID, USER_ID);

        // Assert
        Mockito.verify(commentVoteDao,Mockito.times(1)).insert(COMMENT_ID,USER_ID,VOTE_DOWN);
        Mockito.verify(commentVoteDao,Mockito.never()).delete(Mockito.anyInt());
        Mockito.verify(commentVoteDao,Mockito.never()).update(Mockito.anyInt(),Mockito.anyInt());
    }

    @Test
    public void testVoteUpTwice() {
        // Arrange
        Mockito.when(commentVoteDao.getByCommentAndUser(COMMENT_ID, USER_ID)).thenReturn(Optional.of(new CommentVote(COMMENT_VOTE_MOCK_ID, COMMENT_ID, USER_ID, VOTE_UP)));

        // Act
        commentServiceImplMock.voteUp(COMMENT_ID, USER_ID);

        // Assert
        Mockito.verify(commentVoteDao,Mockito.times(1)).delete(COMMENT_ID);
        Mockito.verify(commentVoteDao,Mockito.never()).insert(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt());
        Mockito.verify(commentVoteDao,Mockito.never()).update(Mockito.anyInt(),Mockito.anyInt());
    }

    @Test
    public void testVoteDownTwice() {
        // Arrange
        Mockito.when(commentVoteDao.getByCommentAndUser(COMMENT_ID, USER_ID)).thenReturn(Optional.of(new CommentVote(COMMENT_VOTE_MOCK_ID, COMMENT_ID, USER_ID, VOTE_DOWN)));

        // Act
        commentServiceImplMock.voteDown(COMMENT_ID, USER_ID);

        // Assert
        Mockito.verify(commentVoteDao,Mockito.times(1)).delete(COMMENT_ID);
        Mockito.verify(commentVoteDao,Mockito.never()).insert(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt());
        Mockito.verify(commentVoteDao,Mockito.never()).update(Mockito.anyInt(),Mockito.anyInt());
    }

    @Test
    public void testVoteUpOnVotedDown() {
        // Arrange
        Mockito.when(commentVoteDao.getByCommentAndUser(COMMENT_ID, USER_ID)).thenReturn(Optional.of(new CommentVote(COMMENT_VOTE_MOCK_ID, COMMENT_ID, USER_ID, VOTE_DOWN)));

        // Act
        commentServiceImplMock.voteUp(COMMENT_ID, USER_ID);

        // Assert
        Mockito.verify(commentVoteDao,Mockito.times(1)).update(COMMENT_ID, VOTE_UP);
        Mockito.verify(commentVoteDao,Mockito.never()).insert(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt());
        Mockito.verify(commentVoteDao,Mockito.never()).delete(Mockito.anyInt());
    }

    @Test
    public void testVoteDownOnVotedUp() {
        // Arrange
        Mockito.when(commentVoteDao.getByCommentAndUser(COMMENT_ID, USER_ID)).thenReturn(Optional.of(new CommentVote(COMMENT_VOTE_MOCK_ID, COMMENT_ID, USER_ID, VOTE_UP)));

        // Act
        commentServiceImplMock.voteDown(COMMENT_ID, USER_ID);

        // Assert
        Mockito.verify(commentVoteDao,Mockito.times(1)).update(COMMENT_ID, VOTE_DOWN);
        Mockito.verify(commentVoteDao,Mockito.never()).insert(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt());
        Mockito.verify(commentVoteDao,Mockito.never()).delete(Mockito.anyInt());
    }

}
