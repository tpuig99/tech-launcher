package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.FrameworkVote;
import ar.edu.itba.paw.persistence.FrameworkVoteDao;
import ar.edu.itba.paw.service.FrameworkVoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FrameworkVoteServiceImpl implements FrameworkVoteService {
    @Autowired
    FrameworkVoteDao vs;
    @Override
    public List<FrameworkVote> getAll() {
        return vs.getAll();
    }

    @Override
    public List<FrameworkVote> getByFramework(long frameworkId) {
        return vs.getByFramework(frameworkId);
    }

    @Override
    public FrameworkVote getById(long voteId) {
        return vs.getById(voteId);
    }

    @Override
    public FrameworkVote getByFrameworkAndUser(long frameworkId, long userId) {
        return vs.getByFrameworkAndUser(frameworkId,userId);
    }

    @Override
    public FrameworkVote insert(long frameworkId, long userId, int stars) {
        FrameworkVote frameworkVote = getByFrameworkAndUser(frameworkId,userId);
        if(frameworkVote !=null){
            update(frameworkVote.getVoteId(),stars);
            frameworkVote.setStars(stars);
            return frameworkVote;
        }
        return vs.insert(frameworkId,userId,stars);
    }

    @Override
    public int delete(long voteId) {
        return vs.delete(voteId);
    }

    @Override
    public FrameworkVote update(long voteId, int stars) {
        return vs.update(voteId,stars);
    }
}
