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
    VoteDao vs;
    @Override
    public List<Vote> getAll() {
        return vs.getAll();
    }

    @Override
    public List<Vote> getByFramework(long frameworkId) {
        return vs.getByFramework(frameworkId);
    }

    @Override
    public Vote getById(long voteId) {
        return vs.getById(voteId);
    }

    @Override
    public Vote getByFrameworkAndUser(long frameworkId, long userId) {
        return vs.getByFrameworkAndUser(frameworkId,userId);
    }

    @Override
    public Vote insert(long frameworkId, long userId, int stars) {
        Vote vote = getByFrameworkAndUser(frameworkId,userId);
        if(vote!=null){
            update(vote.getVoteId(),stars);
        }
        return vs.insert(frameworkId,userId,stars);
    }

    @Override
    public int delete(long voteId) {
        return vs.delete(voteId);
    }

    @Override
    public Vote update(long voteId, int stars) {
        return vs.update(voteId,stars);
    }
}
