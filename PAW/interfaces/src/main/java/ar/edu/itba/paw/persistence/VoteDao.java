package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Vote;

import java.util.List;

public interface VoteDao {
     List<Vote> getAll();
     List<Vote> getByFramework(long frameworkId);
     Vote getById(long voteId);
     Vote getByFrameworkAndUser(long frameworkId,long userId);
     Vote insert(long frameworkId,long userId,int stars);
     int delete(long voteId);
     Vote update(long voteId,int stars);
}
