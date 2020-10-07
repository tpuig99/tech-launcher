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

    @Test(expected = RuntimeException.class)
    public void testVoteUpNew() {
    // 1. Setup!
        Mockito.when(mockDao.getByContentAndUser(1,1)).thenReturn(Optional.empty());
        Mockito.when(mockDao.insert(1,1,1)).thenThrow(new RuntimeException());

    // 2. "ejercito"
        contentService.voteUp(1,1);
    }

    @Test(expected = RuntimeException.class)
    public void testDownNew() {
    // 1. Setup!
        Mockito.when(mockDao.getByContentAndUser(1,1)).thenReturn(Optional.empty());
        Mockito.when(mockDao.insert(1,1,-1)).thenThrow(new RuntimeException());
    // 2. "ejercito"
        contentService.voteDown(1,1);
    }
    @Test(expected = RuntimeException.class)
    public void testVoteUpOnPreviousDown() {
    // 1. Setup!
        Mockito.when(mockDao.getByContentAndUser(1,1)).thenReturn(Optional.of(new ContentVote(1, 1, 1, -1)));
        Mockito.doThrow(new RuntimeException()).when(mockDao).update(1,1);

    // 2. "ejercito"
        contentService.voteUp(1,1);
    // 3. Asserts!
    }
    @Test(expected = RuntimeException.class)
    public void testVoteDownOnPreviousUp() {
        // 1. Setup!
        Mockito.when(mockDao.getByContentAndUser(1,1)).thenReturn(Optional.of(new ContentVote(1, 1, 1, 1)));
        Mockito.doThrow(new RuntimeException()).when(mockDao).update(1,-1);

        // 2. "ejercito"
        contentService.voteDown(1,1);
        // 3. Asserts!
    }
    @Test(expected = RuntimeException.class)
    public void testVoteUpOnPreviousUp() {
        // 1. Setup!
        Mockito.when(mockDao.getByContentAndUser(1,1)).thenReturn(Optional.of(new ContentVote(1, 1, 1, 1)));
        Mockito.doThrow(new RuntimeException()).when(mockDao).delete(1);

        // 2. "ejercito"
        contentService.voteUp(1,1);
        // 3. Asserts!
    }
    @Test(expected = RuntimeException.class)
    public void testVoteDownOnPreviousDown() {
        // 1. Setup!
        Mockito.when(mockDao.getByContentAndUser(1,1)).thenReturn(Optional.of(new ContentVote(1, 1, 1, -1)));
        Mockito.doThrow(new RuntimeException()).when(mockDao).delete(1);

        // 2. "ejercito"
        contentService.voteDown(1,1);
        // 3. Asserts!
    }
}
