package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Vote;
import ar.edu.itba.paw.persistence.VoteDao;
import ar.edu.itba.paw.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VoteServiceImpl implements VoteService {
    @Autowired
    VoteDao vote;
    @Override
    public List<Vote> getAll() {
        return vote.getAll();
    }

    @Override
    public List<Vote> getByFramework(long frameworkId) {
        return vote.getByFramework(frameworkId);
    }

    @Override
    public Vote getById(long voteId) {
        return vote.getById(voteId);
    }

    @Override
    public Vote getByFrameworkAndUser(long frameworkId, long userId) {
        return vote.getByFrameworkAndUser(frameworkId,userId);
    }

    @Override
    public Vote insert(long frameworkId, long userId, int stars) {
        return vote.insert(frameworkId,userId,stars);
    }

    @Override
    public int delete(long voteId) {
        return vote.delete(voteId);
    }

    @Override
    public Vote update(long voteId, int stars) {
        return vote.update(voteId,stars);
    }
}
