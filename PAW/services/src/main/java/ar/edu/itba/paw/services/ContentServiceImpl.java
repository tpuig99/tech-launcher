package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.persistence.ContentDao;
import ar.edu.itba.paw.persistence.ContentVoteDao;
import ar.edu.itba.paw.persistence.ReportContentDao;
import ar.edu.itba.paw.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.List;
import java.util.Optional;

@Service
public class ContentServiceImpl implements ContentService {

    @Autowired
    ContentDao content;

    @Autowired
    ContentVoteDao cv;

    @Autowired
    ReportContentDao rc;

    @Override
    public Optional<Content> getById(long contentId) {
        return content.getById(contentId);
    }

    @Override
    public List<Content> getContentByFramework(long frameworkId) {
        return content.getContentByFramework(frameworkId);
    }

    @Override
    public List<Content> getContentByFrameworkAndUser(long frameworkId, long userId) {
        return content.getContentByFrameworkAndUser(frameworkId, userId);
    }

    @Override
    public List<Content> getContentByUser(long userId) {
        return content.getContentByUser(userId);
    }

    @Override
    public List<Content> getContentByFrameworkAndType(long frameworkId, ContentTypes type) {
        return content.getContentByFrameworkAndType(frameworkId, type);
    }


    @Override
    public Content insertContent(long frameworkId, long userId, String title, String link, ContentTypes type) {
        return content.insertContent(frameworkId, userId, title, link, type);
    }

    @Override
    public int deleteContent(long contentId) {
        return content.deleteContent(contentId);
    }

    @Override
    public Optional<Content> changeContent(long contentId, String title, String link, ContentTypes types) {
        return content.changeContent(contentId, title, link, types);
    }

    @Override
    public void voteUp(long contentId, long userId) {
        Optional<ContentVote> vote = cv.getByContentAndUser(contentId,userId);
        if(vote.isPresent()){
            if(vote.get().isVoteUp())
                cv.delete(vote.get().getContentVoteId());
            else
                cv.update(vote.get().getContentVoteId(),1);
        }else {
            cv.insert(contentId, userId, 1);
        }
        Optional<Content> ct = content.getById(contentId);
    }

    @Override
    public void voteDown(long contentId, long userId) {
        Optional<ContentVote> vote = cv.getByContentAndUser(contentId,userId);
        if(vote.isPresent()){
            if(!vote.get().isVoteUp())
                cv.delete(vote.get().getContentVoteId());
            else
                cv.update(vote.get().getContentVoteId(),-1);
        }else {
            cv.insert(contentId, userId, -1);
        }
        Optional<Content> ct = content.getById(contentId);
    }

    @Override
    public Optional<ReportContent> getReporstById(long reportId) {
        return rc.getById(reportId);
    }

    @Override
    public List<ReportContent> getAllReports() {
        return rc.getAll();
    }

    @Override
    public List<ReportContent> getReportsByFramework(long frameworkId) {
        return rc.getByFramework(frameworkId);
    }

    @Override
    public Optional<ReportContent> getReportsByContent(long contentId) {
        return rc.getByContent(contentId);
    }

    @Override
    public void addReport(long contentId, long userId, String description) {
        rc.add(contentId,userId,description);
    }

    @Override
    public void deleteReport(long reportId) {
        rc.delete(reportId);
    }

    @Override
    public void deleteReportByContent(long contentId) {
        rc.deleteByContent(contentId);
    }

}
