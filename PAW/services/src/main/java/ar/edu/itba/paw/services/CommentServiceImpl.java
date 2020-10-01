package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Comment;
import ar.edu.itba.paw.models.CommentVote;
import ar.edu.itba.paw.models.ReportComment;
import ar.edu.itba.paw.models.VerifyUser;
import ar.edu.itba.paw.persistence.CommentDao;
import ar.edu.itba.paw.persistence.CommentVoteDao;
import ar.edu.itba.paw.persistence.ReportCommentDao;
import ar.edu.itba.paw.persistence.VerifyUserDao;
import ar.edu.itba.paw.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {
    private static int VOTES_FOR_VERIFY =10;

    @Autowired
    CommentDao cmts;

    @Autowired
    VerifyUserDao verifyUserDao;

    @Autowired
    CommentVoteDao cmtVotes;

    @Autowired
    ReportCommentDao rc;

    @Override
    public Optional<Comment> getById(long contentId) {
        Optional<Comment> comment = cmts.getById(contentId);
        return comment;
    }

    @Override
    public List<Comment> getCommentsByFramework(long frameworkId,Long userId) {
        List<Comment> comments = cmts.getCommentsByFramework(frameworkId,userId);
        return comments;
    }

    @Override
    public List<Comment> getCommentsWithoutReferenceByFramework(long frameworkId) {
        List<Comment> comments = cmts.getCommentsWithoutReferenceByFramework(frameworkId);
        return comments;
    }

    @Override
    public List<Comment> getCommentsByFrameworkAndUser(long frameworkId, long userId) {
        List<Comment> comments = cmts.getCommentsByFrameworkAndUser(frameworkId, userId);
        return comments;
    }

    @Override
    public List<Comment> getCommentsByUser(long userId) {
        List<Comment> comments = cmts.getCommentsByUser(userId);
        return comments;
    }

    @Override
    public Map<Long, List<Comment>> getRepliesByFramework(long frameworkId) {
        return cmts.getRepliesByFramework(frameworkId);
    }

    @Override
    public Comment insertComment(long frameworkId, long userId, String description, Long reference) {
       Optional<Comment> comment = cmts.insertComment(frameworkId, userId, description, reference);
       if(comment.isPresent())
           return comment.get();
       return null;
    }

    @Override
    public int deleteComment(long commentId) {
        return cmts.deleteComment(commentId);
    }

    @Override
    public Optional<Comment> changeComment(long commentId, String description) {
        Optional<Comment> comment = cmts.changeComment(commentId, description);
        return comment;
    }

    @Override
    public Optional<Comment> voteUp(long commentId,long userId) {
        Optional<CommentVote> vote = cmtVotes.getByCommentAndUser(commentId,userId);
        if(vote.isPresent()){
            cmtVotes.update(vote.get().getCommentVoteId(),1);
        }else {
            cmtVotes.insert(commentId, userId, 1);
        }
        Optional<Comment> comment = cmts.getById(commentId);
        checkForVerify(comment);
        return comment;
    }

    private void checkForVerify(Optional<Comment> comment) {
        if(comment.isPresent() && comment.get().getVotesUp()==VOTES_FOR_VERIFY){
            Comment c=comment.get();
            Optional<VerifyUser> v = verifyUserDao.getByFrameworkAndUser(c.getFrameworkId(),c.getUserId());
            if(!v.isPresent()){
                verifyUserDao.create(c.getUserId(),c.getFrameworkId(),c.getCommentId());
            }
        }
    }

    @Override
    public Optional<Comment> voteDown(long commentId,long userId) {
        Optional<CommentVote> vote = cmtVotes.getByCommentAndUser(commentId,userId);

        if(vote.isPresent()){
            cmtVotes.update(vote.get().getCommentVoteId(),-1);
        }else {
            cmtVotes.insert(commentId, userId, -1);
        }

        Optional<Comment> comment = cmts.getById(commentId);
        return comment;
    }

    @Override
    public Optional<ReportComment> getReportById(long reportId) {
        return Optional.empty();
    }

    @Override
    public List<ReportComment> getAllReport() {
        return rc.getAll();
    }

    @Override
    public List<ReportComment> getReportByFramework(long frameworkId) {
        return rc.getByFramework(frameworkId);
    }

    @Override
    public Optional<ReportComment> getReportByComment(long commentId) {
        return rc.getByComment(commentId);
    }

    @Override
    public void addReport(long commentId, long userId, String description) {
        rc.add(commentId,userId,description);
    }

    @Override
    public void deleteReport(long reportId) {
        rc.delete(reportId);
    }

}
