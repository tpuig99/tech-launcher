package ar.edu.itba.paw;

import ar.edu.itba.paw.models.ContentVote;
import ar.edu.itba.paw.persistence.ContentVoteDao;
import ar.edu.itba.paw.services.ContentServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class ContentServiceTest {
    @InjectMocks
    private ContentServiceImpl contentService = new ContentServiceImpl();
    @Mock
    private ContentVoteDao mockDao;
    private static int POS = 1;
    private static int NEG = -1;
    private static long USER_ID = 1;
    private static long CONTENT_ID = 1;
    private static long VOTE_ID = 1;


    @Test()
    public void testVoteUpNew() {
        Mockito.when(mockDao.getByContentAndUser(CONTENT_ID,USER_ID)).thenReturn(Optional.empty());

        contentService.voteUp(CONTENT_ID,USER_ID);

        Mockito.verify(mockDao,Mockito.times(1)).insert(CONTENT_ID,USER_ID,POS);
        Mockito.verify(mockDao,Mockito.never()).delete(VOTE_ID);
        Mockito.verify(mockDao,Mockito.never()).update(VOTE_ID,POS);

    }

    @Test()
    public void testDownNew() {
        Mockito.when(mockDao.getByContentAndUser(CONTENT_ID,USER_ID)).thenReturn(Optional.empty());

        contentService.voteDown(1,1);

        Mockito.verify(mockDao,Mockito.times(1)).insert(CONTENT_ID,USER_ID,NEG);
        Mockito.verify(mockDao,Mockito.never()).delete(VOTE_ID);
        Mockito.verify(mockDao,Mockito.never()).update(VOTE_ID,NEG);

    }
    @Test()
    public void testVoteUpOnPreviousDown() {
        Mockito.when(mockDao.getByContentAndUser(CONTENT_ID,USER_ID)).thenReturn(Optional.of(new ContentVote(VOTE_ID,CONTENT_ID,USER_ID,NEG )));

        contentService.voteUp(CONTENT_ID,USER_ID);

        Mockito.verify(mockDao,Mockito.never()).insert(CONTENT_ID,USER_ID,POS);
        Mockito.verify(mockDao,Mockito.never()).delete(VOTE_ID);
        Mockito.verify(mockDao,Mockito.times(1)).update(VOTE_ID,POS);

    }
    @Test()
    public void testVoteDownOnPreviousUp() {
        Mockito.when(mockDao.getByContentAndUser(CONTENT_ID,USER_ID)).thenReturn(Optional.of(new ContentVote(VOTE_ID,CONTENT_ID,USER_ID,POS)));

        contentService.voteDown(CONTENT_ID,USER_ID);

        Mockito.verify(mockDao,Mockito.never()).insert(CONTENT_ID,USER_ID,NEG);
        Mockito.verify(mockDao,Mockito.never()).delete(VOTE_ID);
        Mockito.verify(mockDao,Mockito.times(1)).update(VOTE_ID,NEG);

    }
    @Test()
    public void testVoteUpOnPreviousUp() {
        Mockito.when(mockDao.getByContentAndUser(CONTENT_ID,USER_ID)).thenReturn(Optional.of(new ContentVote(VOTE_ID,CONTENT_ID,USER_ID,POS )));

        contentService.voteUp(CONTENT_ID,USER_ID);

        Mockito.verify(mockDao,Mockito.never()).insert(CONTENT_ID,USER_ID,POS);
        Mockito.verify(mockDao,Mockito.times(1)).delete(VOTE_ID);
        Mockito.verify(mockDao,Mockito.never()).update(VOTE_ID,POS);
    }
    @Test()
    public void testVoteDownOnPreviousDown() {
        Mockito.when(mockDao.getByContentAndUser(CONTENT_ID,USER_ID)).thenReturn(Optional.of(new ContentVote(VOTE_ID,CONTENT_ID,USER_ID,NEG )));

        contentService.voteDown(CONTENT_ID,USER_ID);

        Mockito.verify(mockDao,Mockito.never()).insert(CONTENT_ID,USER_ID,NEG);
        Mockito.verify(mockDao,Mockito.times(1)).delete(VOTE_ID);
        Mockito.verify(mockDao,Mockito.never()).update(VOTE_ID,NEG);

    }
}
