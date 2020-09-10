package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.FrameworkVote;

import java.util.List;

public interface FrameworkVoteDao {
     List<FrameworkVote> getAll();
     List<FrameworkVote> getByFramework(long frameworkId);
     FrameworkVote getById(long voteId);
     FrameworkVote getByFrameworkAndUser(long frameworkId, long userId);
     FrameworkVote insert(long frameworkId, long userId, int stars);
     int delete(long voteId);
     FrameworkVote update(long voteId, int stars);
}
