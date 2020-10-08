package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.persistence.ContentDao;
import ar.edu.itba.paw.persistence.ContentVoteDao;
import ar.edu.itba.paw.persistence.ReportContentDao;
import ar.edu.itba.paw.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private long PAGESIZE = 5;

    @Transactional(readOnly = true)
    @Override
    public Optional<Content> getById(long contentId) {
        return content.getById(contentId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Content> getContentByFramework(long frameworkId) {
        return content.getContentByFramework(frameworkId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Content> getContentByFrameworkAndUser(long frameworkId, long userId) {
        return content.getContentByFrameworkAndUser(frameworkId, userId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Content> getContentByUser(long userId, long page) {
        return content.getContentByUser(userId, page, PAGESIZE );
    }

    @Transactional(readOnly = true)
    @Override
    public List<Content> getContentByFrameworkAndType(long frameworkId, ContentTypes type) {
        return content.getContentByFrameworkAndType(frameworkId, type);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Content> getContentByFrameworkAndTypeAndTitle(long frameworkId, ContentTypes type, String title) {
        return content.getContentByFrameworkAndTypeAndTitle(frameworkId,type,title);
    }

    @Transactional
    @Override
    public Content insertContent(long frameworkId, long userId, String title, String link, ContentTypes type) {
        return content.insertContent(frameworkId, userId, title, link, type);
    }

    @Transactional
    @Override
    public int deleteContent(long contentId) {
        return content.deleteContent(contentId);
    }

    @Transactional
    @Override
    public Optional<Content> changeContent(long contentId, String title, String link, ContentTypes types) {
        return content.changeContent(contentId, title, link, types);
    }

    @Transactional
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
    }

    @Transactional
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

    @Transactional(readOnly = true)
    @Override
    public Optional<ReportContent> getReporstById(long reportId) {
        return rc.getById(reportId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ReportContent> getAllReports() {
        return rc.getAll();
    }

    @Transactional(readOnly = true)
    @Override
    public List<ReportContent> getReportsByFramework(long frameworkId) {
        return rc.getByFramework(frameworkId);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<ReportContent> getReportsByContent(long contentId) {
        return rc.getByContent(contentId);
    }

    @Transactional
    @Override
    public void addReport(long contentId, long userId, String description) {
        rc.add(contentId,userId,description);
    }

    @Transactional
    @Override
    public void acceptReport(long contentId) {
        content.deleteContent(contentId);
    }

    @Transactional
    @Override
    public void denyReport(long contentId) {
        rc.deleteByContent(contentId);
    }

    @Transactional
    @Override
    public void deleteReport(long reportId) {
        rc.delete(reportId);
    }

}
