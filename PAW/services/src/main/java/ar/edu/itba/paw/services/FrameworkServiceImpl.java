package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.persistence.FrameworkDao;
import ar.edu.itba.paw.service.CommentService;
import ar.edu.itba.paw.service.ContentService;
import ar.edu.itba.paw.service.FrameworkService;
import ar.edu.itba.paw.service.FrameworkVoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FrameworkServiceImpl implements FrameworkService {

    @Autowired
    private FrameworkDao frameworkDao;

    @Autowired
    private FrameworkVoteService vote;
    @Autowired
    private CommentService cmts;
    @Autowired
    private ContentService ctns;

    @Override
    public Optional<Framework> findById(long id) {
        Optional<Framework> framework = frameworkDao.findById(id);
        framework.ifPresent(this::getStarsAndVotes);
        return framework;
    }

    @Override
    public List<Framework> getByCategory(FrameworkCategories category) {
        return frameworkDao.getByCategory(category);
    }

    @Override
    public List<Framework> getByType(FrameworkType type) {
        return frameworkDao.getByType(type);
    }

    @Override
    public List<Framework> getByWord(String toSearch) {
        return frameworkDao.getByWord(toSearch);
    }

    @Override
    public List<Framework> search(String toSearch, FrameworkCategories category, FrameworkType type) {
        if(toSearch!=null && category==null && type==null){
            return  frameworkDao.getByWord(toSearch);
        }
        if(toSearch!=null && category!=null && type==null){
            return  frameworkDao.getByCategoryAndWord(category,toSearch);
        }
        if(toSearch!=null && category==null && type!=null){
            return  frameworkDao.getByTypeAndWord(type,toSearch);
        }
        if(toSearch!=null && category!=null && type!=null){
            return  frameworkDao.getByCategoryAndTypeAndWord(type,category,toSearch);
        }
        if(toSearch==null && category==null && type!=null){
            return  frameworkDao.getByType(type);
        }
        if(toSearch==null && category!=null && type!=null){
            return  frameworkDao.getByCategoryAndType(type,category);
        }
        if(toSearch==null && category!=null && type==null){
            return  frameworkDao.getByCategory(category);
        }
        return new ArrayList<Framework>();
    }

    @Override
    public List<Framework> getAll() {
        return frameworkDao.getAll();
    }

    @Override
    public double getStars(long frameworkId) {
        List<FrameworkVote> frameworkVotes =  vote.getByFramework(frameworkId);
        if(frameworkVotes.isEmpty()){
            return 0;
        }
        double sum = 0,count=0;
        for (FrameworkVote frameworkVote : frameworkVotes) {
            sum+= frameworkVote.getStars();
            count++;
        }

        if (count == 0) {
            return count;
        }

        return sum/count;
    }

    @Override
    public List<Comment> getComments(long id) {
        return cmts.getCommentsByFramework(id);
    }

    @Override
    public List<Content> getContent(long id) {
        return ctns.getContentByFramework(id);
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

    @Override
    public Content insertContent(long frameworkId, long userId, String title, String link, ContentTypes type, Boolean pending) {
        return ctns.insertContent(frameworkId,userId,title,link,type, pending);
    }

    @Override
    public int deleteContent(long contentId) {
        return ctns.deleteContent(contentId);
    }

    @Override
    public Optional<Content> changeContent(long contentId, String title, String link, ContentTypes types) {
        return ctns.changeContent(contentId,title,link,types);
    }

    @Override
    public Comment insertComment(long frameworkId, long userId, String description) {
        return cmts.insertComment(frameworkId,userId,description,null);
    }

    @Override
    public int deleteComment(long commentId) {
        return cmts.deleteComment(commentId);
    }

    @Override
    public Optional<Comment> changeComment(long commentId, String description) {
        return cmts.changeComment(commentId,description);
    }

    private void getStarsAndVotes(Framework framework){
        List<FrameworkVote> frameworkVotes =  vote.getByFramework(framework.getId());
        if(frameworkVotes.isEmpty()){
            framework.setStars(0);
            framework.setVotesCant(0);
            return;
        }
        double sum = 0,count=0;
        for (FrameworkVote frameworkVote : frameworkVotes) {
            sum+= frameworkVote.getStars();
            count++;
        }
        framework.setVotesCant((int) count);
        framework.setStars(sum/count);
    }

}
