package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Comment;
import ar.edu.itba.paw.models.Content;
import ar.edu.itba.paw.models.Vote;

import java.sql.Timestamp;
import java.util.List;

public interface CommentDao {
    Comment getById(long contentId);
    List<Comment> getCommentsByFramework(long frameworkId);
    List<Comment> getCommentsByFrameworkAndUser(long frameworkId, long userId);
    List<Comment> getCommentsByUser(long userId);
    Comment insertComment(long frameworkId, long userId, String description, long reference);
    int deleteComment(long commentId);
    Comment changeComment(long commentId, String description);
}
