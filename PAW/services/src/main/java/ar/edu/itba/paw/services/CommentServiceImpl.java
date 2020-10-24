package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.persistence.*;
import ar.edu.itba.paw.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {
    private static final int VOTES_FOR_VERIFY =10;

    @Autowired
    private CommentDao cmts;

    @Autowired
    private UserDao userDao;

    @Autowired
    private VerifyUserDao verifyUserDao;

    @Autowired
    private CommentVoteDao cmtVotes;

    @Autowired
    private ReportCommentDao rc;

    private long PAGESIZE = 5;

    @Transactional(readOnly = true)
    @Override
    public Optional<Comment> getById(long contentId) {
        return cmts.getById(contentId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Comment> getCommentsByFramework(long frameworkId,Long userId) {
        return cmts.getCommentsByFramework(frameworkId,userId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Comment> getCommentsWithoutReferenceByFrameworkWithUser(long frameworkId,Long userId, long page) {
        return cmts.getCommentsWithoutReferenceByFrameworkWithUser(frameworkId,userId, page, PAGESIZE);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Comment> getCommentsByFrameworkAndUser(long frameworkId, long userId) {
        return cmts.getCommentsByFrameworkAndUser(frameworkId, userId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Comment> getCommentsByUser(long userId, long page) {
        return cmts.getCommentsByUser(userId, page, PAGESIZE);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Integer> getCommentsCountByUser(long userId){
        return cmts.getCommentsCountByUser(userId);
    }

    @Transactional(readOnly = true)
    @Override
    public Map<Long, List<Comment>> getRepliesByFramework(long frameworkId) {
        return cmts.getRepliesByFramework(frameworkId);
    }

    @Transactional
    @Override
    public Comment insertComment(long frameworkId, long userId, String description, Long reference) {
       return cmts.insertComment(frameworkId, userId, description, reference);
    }

    @Transactional
    @Override
    public void deleteComment(long commentId) {
        cmts.deleteComment(commentId);
    }

    @Transactional
    @Override
    public Optional<Comment> changeComment(long commentId, String description) {
        return cmts.changeComment(commentId, description);
    }

    @Transactional
    @Override
    public Optional<Comment> vote(long commentId,long userId,int voteSign) {
        Optional<CommentVote> vote = cmtVotes.getByCommentAndUser(commentId,userId);
        if(vote.isPresent()){
            if(vote.get().getVote()==voteSign)
                cmtVotes.delete(vote.get().getCommentVoteId());
            else
                cmtVotes.update(vote.get().getCommentVoteId(),voteSign);
        }else {
            cmtVotes.insert(commentId, userId, voteSign);
        }
        Optional<Comment> comment = cmts.getById(commentId);
        if(voteSign>0)
            checkForVerify(comment);
        return comment;
    }
    private void checkForVerify(Optional<Comment> comment) {
        if(comment.isPresent() && comment.get().getVotesUp()==VOTES_FOR_VERIFY){
            Comment c=comment.get();
            Optional<User> user = userDao.findById(c.getUserId());
            if(user.isPresent() && user.get().isAllowMod() && !user.get().isAdmin() && !user.get().hasAppliedToFramework(c.getFrameworkId())) {
                    verifyUserDao.create(c.getUserId(), c.getFrameworkId(), c.getCommentId());

            }
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<ReportComment> getReportById(long reportId) {
        return Optional.empty();
    }

    @Transactional(readOnly = true)
    @Override
    public List<ReportComment> getAllReport(long page) {
        return rc.getAll(page, PAGESIZE);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ReportComment> getReportByFramework(long frameworkId) {
        return rc.getByFramework(frameworkId);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<ReportComment> getReportByComment(long commentId) {
        return rc.getByComment(commentId);
    }

    @Transactional
    @Override
    public void addReport(long commentId, long userId, String description) {
        rc.add(commentId,userId,description);
    }

    @Transactional
    @Override
    public void acceptReport(long commentId) {
        cmts.deleteComment(commentId);
    }

    @Transactional
    @Override
    public void denyReport(long commentId) {
        rc.deleteReportByComment(commentId);
    }

    @Transactional
    @Override
    public void deleteReport(long reportId) {
        rc.delete(reportId);
    }


}
