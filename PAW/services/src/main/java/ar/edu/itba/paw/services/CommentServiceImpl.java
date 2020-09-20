package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Comment;
import ar.edu.itba.paw.models.CommentVote;
import ar.edu.itba.paw.persistence.CommentDao;
import ar.edu.itba.paw.persistence.CommentVoteDao;
import ar.edu.itba.paw.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    CommentDao cmts;
    @Autowired
    CommentVoteDao cmtVotes;

    @Override
    public Optional<Comment> getById(long contentId) {
        Optional<Comment> comment = cmts.getById(contentId);
        return comment;
    }

    @Override
    public List<Comment> getCommentsByFramework(long frameworkId) {
        List<Comment> comments = cmts.getCommentsByFramework(frameworkId);
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
        return comment;
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

}
