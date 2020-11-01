package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.FrameworkVote;
import ar.edu.itba.paw.persistence.FrameworkVoteDao;
import ar.edu.itba.paw.service.FrameworkVoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class FrameworkVoteServiceImpl implements FrameworkVoteService {
    @Autowired
    FrameworkVoteDao vs;

    private final long PAGE_SIZE = 10;

    @Transactional(readOnly = true)
    @Override
    public Optional<FrameworkVote> getByFrameworkAndUser(long frameworkId, long userId) {
        return vs.getByFrameworkAndUser(frameworkId,userId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<FrameworkVote> getAllByUser(long userId, long page) {
        return vs.getAllByUser(userId, page, PAGE_SIZE);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Integer> getAllCountByUser( long userId ){
        return vs.getAllCountByUser(userId);
    }


    @Transactional
    @Override
    public FrameworkVote insert(long frameworkId, long userId, int stars) {
        Optional<FrameworkVote> frameworkVote = getByFrameworkAndUser(frameworkId,userId);
        if(frameworkVote.isPresent()){
            update(frameworkVote.get().getVoteId(),stars);
            frameworkVote.get().setStars(stars);
            return frameworkVote.get();
        }
        return vs.insert(frameworkId,userId,stars);
    }

    @Transactional
    @Override
    public void delete(long voteId) {
         vs.delete(voteId);
    }

    @Transactional
    @Override
    public Optional<FrameworkVote> update(long voteId, int stars) {
        return vs.update(voteId,stars);
    }
}
