package ar.edu.itba.paw;

import ar.edu.itba.paw.models.CommentVote;
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

    @Test(expected = RuntimeException.class)
    public void testVoteUp() {
        Mockito.when(commentVoteDao.getByCommentAndUser(COMMENT_ID, USER_ID)).thenReturn(Optional.empty());
        Mockito.doThrow(new RuntimeException()).when(commentVoteDao).insert(COMMENT_ID, USER_ID, VOTE_UP);

        commentServiceImplMock.voteUp(COMMENT_ID, USER_ID);
    }

    @Test(expected = RuntimeException.class)
    public void testVoteDown() {
        Mockito.when(commentVoteDao.getByCommentAndUser(COMMENT_ID, USER_ID)).thenReturn(Optional.empty());
        Mockito.doThrow(new RuntimeException()).when(commentVoteDao).insert(COMMENT_ID, USER_ID, VOTE_DOWN);

        commentServiceImplMock.voteDown(COMMENT_ID, USER_ID);
    }

    @Test(expected = RuntimeException.class)
    public void testVoteUpTwice() {
        Mockito.when(commentVoteDao.getByCommentAndUser(COMMENT_ID, USER_ID)).thenReturn(Optional.of(new CommentVote(COMMENT_VOTE_MOCK_ID, COMMENT_ID, USER_ID, VOTE_UP)));
        Mockito.doThrow(new RuntimeException()).when(commentVoteDao).delete(COMMENT_VOTE_MOCK_ID);

        commentServiceImplMock.voteUp(COMMENT_ID, USER_ID);
    }

    @Test(expected = RuntimeException.class)
    public void testVoteDownTwice() {
        Mockito.when(commentVoteDao.getByCommentAndUser(COMMENT_ID, USER_ID)).thenReturn(Optional.of(new CommentVote(COMMENT_VOTE_MOCK_ID, COMMENT_ID, USER_ID, VOTE_DOWN)));
        Mockito.doThrow(new RuntimeException()).when(commentVoteDao).delete(COMMENT_VOTE_MOCK_ID);

        commentServiceImplMock.voteDown(COMMENT_ID, USER_ID);
    }

    @Test(expected = RuntimeException.class)
    public void testVoteUpOnVotedDown() {
        Mockito.when(commentVoteDao.getByCommentAndUser(COMMENT_ID, USER_ID)).thenReturn(Optional.of(new CommentVote(COMMENT_VOTE_MOCK_ID, COMMENT_ID, USER_ID, VOTE_DOWN)));
        Mockito.doThrow(new RuntimeException()).when(commentVoteDao).update(COMMENT_VOTE_MOCK_ID, VOTE_UP);

        commentServiceImplMock.voteUp(COMMENT_ID, USER_ID);
    }

    @Test(expected = RuntimeException.class)
    public void testVoteDownOnVotedUp() {
        Mockito.when(commentVoteDao.getByCommentAndUser(COMMENT_ID, USER_ID)).thenReturn(Optional.of(new CommentVote(COMMENT_VOTE_MOCK_ID, COMMENT_ID, USER_ID, VOTE_UP)));
        Mockito.doThrow(new RuntimeException()).when(commentVoteDao).update(COMMENT_VOTE_MOCK_ID, VOTE_DOWN);

        commentServiceImplMock.voteDown(COMMENT_ID, USER_ID);
    }

}
