package ar.edu.itba.paw.service;

import ar.edu.itba.paw.models.Comment;

import java.util.List;

public interface CommentService {
    Comment getById(long contentId);
    List<Comment> getCommentsByFramework(long frameworkId);
    List<Comment> getCommentsByFrameworkAndUser(long frameworkId, long userId);
    List<Comment> getCommentsByUser(long userId);
    Comment insertComment(long frameworkId, long userId, String description, Long reference);
    int deleteComment(long commentId);
    Comment changeComment(long commentId, String description);
    Comment voteUp(long commentId,long userId);
    Comment voteDown(long commentId,long userId);
}
