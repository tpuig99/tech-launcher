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
        comment.ifPresent(this::setCommentVotes);
        return comment;
    }

    @Override
    public List<Comment> getCommentsByFramework(long frameworkId) {
        List<Comment> comments = cmts.getCommentsByFramework(frameworkId);
        for (Comment comment:comments) {
            setCommentVotes(comment);
        }
        return comments;
    }

    @Override
    public List<Comment> getCommentsByFrameworkAndUser(long frameworkId, long userId) {
        List<Comment> comments = cmts.getCommentsByFrameworkAndUser(frameworkId, userId);
        for (Comment comment:comments) {
            setCommentVotes(comment);
        }
        return comments;
    }

    @Override
    public List<Comment> getCommentsByUser(long userId) {
        List<Comment> comments = cmts.getCommentsByUser(userId);;
        for (Comment comment:comments) {
            setCommentVotes(comment);
        }
        return comments;
    }

    @Override
    public Comment insertComment(long frameworkId, long userId, String description, Long reference) {
       Comment comment = cmts.insertComment(frameworkId, userId, description, reference);
       comment.setVotesDown(0);
       comment.setVotesUp(0);
       return comment;
    }

    @Override
    public int deleteComment(long commentId) {
        List<CommentVote> votes = cmtVotes.getByComment(commentId);
        for (CommentVote vote: votes) {
            cmtVotes.delete(vote.getCommentVoteId());
        }
        return cmts.deleteComment(commentId);
    }

    @Override
    public Optional<Comment> changeComment(long commentId, String description) {
        Optional<Comment> comment = cmts.changeComment(commentId, description);
        comment.ifPresent(this::setCommentVotes);
        return comment;
    }

    @Override
    public Optional<Comment> voteUp(long commentId,long userId) {
        CommentVote vote = cmtVotes.getByCommentAndUser(commentId,userId);
        if(vote!=null){
            vote = cmtVotes.update(vote.getCommentVoteId(),1);
        }else {
            vote = cmtVotes.insert(commentId, userId, 1);
        }
        Optional<Comment> comment = cmts.getById(commentId);
        comment.ifPresent(this::setCommentVotes);
        return comment;
    }

    @Override
    public Optional<Comment> voteDown(long commentId,long userId) {
        CommentVote vote = cmtVotes.getByCommentAndUser(commentId,userId);
        if(vote!=null){
            vote = cmtVotes.update(vote.getCommentVoteId(),-1);
        }else {
            vote = cmtVotes.insert(commentId, userId, -1);
        }
        Optional<Comment> comment = cmts.getById(commentId);
        comment.ifPresent(this::setCommentVotes);
        return comment;
    }
    private void setCommentVotes(Comment comment){
        List<CommentVote> votes = cmtVotes.getByComment(comment.getCommentId());
        int voteUp=0;
        int voteDown=0;
        for (CommentVote vote:votes) {
            if(vote.isVoteUp()){
                voteUp++;
            }else{
                voteDown++;
            }
        }
        comment.setVotesUp(voteUp);
        comment.setVotesDown(voteDown);
    }
}
