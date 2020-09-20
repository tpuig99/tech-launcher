package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Comment;
import ar.edu.itba.paw.models.CommentVote;
import ar.edu.itba.paw.persistence.CommentDao;
import ar.edu.itba.paw.persistence.CommentVoteDao;
import ar.edu.itba.paw.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        setCommentVotes(comments);
        return comments;
    }

    @Override
    public List<Comment> getRepliesByCommentAndFramework(long commentId, long frameworkId) {
        List<Comment> comments = cmts.getRepliesByCommentAndFramework(commentId,frameworkId);
        setCommentVotes(comments);
        return comments;
    }

    @Override
    public List<Comment> getCommentsByFrameworkAndUser(long frameworkId, long userId) {
        List<Comment> comments = cmts.getCommentsByFrameworkAndUser(frameworkId, userId);
        setCommentVotes(comments);
        return comments;
    }

    @Override
    public List<Comment> getCommentsByUser(long userId) {
        List<Comment> comments = cmts.getCommentsByUser(userId);
        setCommentVotes(comments);
        return comments;
    }

    @Override
    public Map<Long, List<Comment>> getRepliesByFramework(long frameworkId) {
        return cmts.getRepliesByFramework(frameworkId);
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
        // TODO: change for an On Delete Cascade. As no comment vote can exists if the comment does not exists
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
        Optional<CommentVote> vote = cmtVotes.getByCommentAndUser(commentId,userId);
        if(vote.isPresent()){
            cmtVotes.update(vote.get().getCommentVoteId(),1);
        }else {
            cmtVotes.insert(commentId, userId, 1);
        }
        Optional<Comment> comment = cmts.getById(commentId);
        comment.ifPresent(this::setCommentVotes);
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
        comment.ifPresent(this::setCommentVotes);
        return comment;
    }

    private void setCommentVotes(List<Comment> comments) {
        for (Comment comment:comments) {
            setCommentVotes(comment);
        }
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
