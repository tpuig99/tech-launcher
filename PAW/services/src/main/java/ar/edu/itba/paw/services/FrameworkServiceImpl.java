package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.persistence.FrameworkDao;
import ar.edu.itba.paw.persistence.VoteDao;
import ar.edu.itba.paw.service.FrameworkService;
import ar.edu.itba.paw.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.List;

@Service
public class FrameworkServiceImpl implements FrameworkService {

    @Autowired
    private FrameworkDao frameworkDao;

    @Autowired
    private VoteService vote;

    @Override
    public Framework findById(long id) {
        Framework framework =frameworkDao.findById(id);
        if(framework!=null) {
           getStarsAndVotes(framework);
        }
        return framework;
    }

    @Override
    public List<Framework> getByCategory(FrameworkCategories category) {
        return frameworkDao.getByCategory(category);
    }

    @Override
    public List<Framework> getAll() {
        return frameworkDao.getAll();
    }

    @Override
    public double getStars(long frameworkId) {
        List<Vote>votes =  vote.getByFramework(frameworkId);
        if(votes==null){
            return 0;
        }
        double sum = 0,count=0;
        for (Vote vote:votes) {
            sum+=vote.getStars();
            count++;
        }

        if (count == 0) {
            return count;
        }

        return sum/count;
    }

    @Override
    public List<Comment> getComments(long id) {
        return null;
    }

    @Override
    public List<Content> getContent(long id) {
        return null;
    }

    @Override
    public int getVotesCant(long frameworkId) {
        return vote.getByFramework(frameworkId).size();
    }

    @Override
    public List<Framework> getCompetitors(Framework framework) {
        List<Framework> competitors = getByCategory(framework.getFrameCategory());
        competitors.remove(framework);
        return competitors;
    }
    private void getStarsAndVotes(Framework framework){
        List<Vote>votes =  vote.getByFramework(framework.getId());
        if(votes==null){
            framework.setStars(0);
            framework.setVotesCant(0);
        }
        double sum = 0,count=0;
        for (Vote vote:votes) {
            sum+=vote.getStars();
            count++;
        }
        framework.setVotesCant((int) count);
        framework.setStars(sum/count);
    }

}
