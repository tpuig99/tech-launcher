package ar.edu.itba.paw.service;

import ar.edu.itba.paw.models.FrameworkVote;

import java.util.List;
import java.util.Optional;

public interface FrameworkVoteService {
    List<FrameworkVote> getByFramework(long frameworkId);
    Optional<FrameworkVote> getById(long voteId);
    Optional<FrameworkVote> getByFrameworkAndUser(long frameworkId, long userId);
    List<FrameworkVote> getAllByUser(long userId);
    FrameworkVote insert(long frameworkId, long userId, int stars);
    int delete(long voteId);
    Optional<FrameworkVote> update(long voteId, int stars);
}
