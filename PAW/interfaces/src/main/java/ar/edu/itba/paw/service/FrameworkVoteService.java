package ar.edu.itba.paw.service;

import ar.edu.itba.paw.models.FrameworkVote;

import java.util.List;
import java.util.Optional;

public interface FrameworkVoteService {
    Optional<FrameworkVote> getByFrameworkAndUser(long frameworkId, long userId);
    List<FrameworkVote> getAllByUser(long userId, long page);

    Optional<Integer> getAllCountByUser(long userId);

    FrameworkVote insert(long frameworkId, long userId, int stars);
    void delete(long voteId);
    Optional<FrameworkVote> update(long voteId, int stars);
}
