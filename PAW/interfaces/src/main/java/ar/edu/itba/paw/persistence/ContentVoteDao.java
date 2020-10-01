package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.ContentVote;

import java.util.List;
import java.util.Optional;

public interface ContentVoteDao {
    Optional<ContentVote> getById(long voteId);
    Optional<ContentVote> getByContentAndUser(long contentId, long userId);
    ContentVote insert(long contentId, long userId, int vote);
    int delete(long voteId);
    void update(long voteId, int vote);
}
