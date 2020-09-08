package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Comment;
import ar.edu.itba.paw.persistence.CommentDao;
import ar.edu.itba.paw.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    CommentDao comment;

    @Override
    public Comment getById(long contentId) {
        return comment.getById(contentId);
    }

    @Override
    public List<Comment> getCommentsByFramework(long frameworkId) {
        return comment.getCommentsByFramework(frameworkId);
    }

    @Override
    public List<Comment> getCommentsByFrameworkAndUser(long frameworkId, long userId) {
        return comment.getCommentsByFrameworkAndUser(frameworkId, userId);
    }

    @Override
    public List<Comment> getCommentsByUser(long userId) {
        return comment.getCommentsByUser(userId);
    }

    @Override
    public Comment insertComment(long frameworkId, long userId, String description, long reference) {
        return comment.insertComment(frameworkId, userId, description, reference);
    }

    @Override
    public int deleteComment(long commentId) {
        return comment.deleteComment(commentId);
    }

    @Override
    public Comment changeComment(long commentId, String description) {
        return comment.changeComment(commentId, description);
    }

    @Override
    public Comment voteUp(long commentId) {
        return comment.voteUp(commentId);
    }

    @Override
    public Comment voteDown(long commentId) {
        return comment.voteDown(commentId);
    }
}
