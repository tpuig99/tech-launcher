package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Vote;

import java.util.List;

public interface VoteDao {
     List<Vote> getVotes();
     Vote getVote(int frameworkId,int userId);
     Vote insertVote(int frameworkId,int userId,int stars);
     Vote deleteVote(int voteId);
     Vote changeVote(int voteId,int stars);
}
