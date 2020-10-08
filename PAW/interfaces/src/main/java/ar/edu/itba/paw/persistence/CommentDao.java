package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Comment;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CommentDao {
    Optional<Comment> getById(long contentId);
    List<Comment> getCommentsByFramework(long frameworkId,Long userId);
    List<Comment> getCommentsWithoutReferenceByFrameworkWithUser(long frameworkId,Long userId);
    List<Comment> getCommentsByFrameworkAndUser(long frameworkId, long userId);
    List<Comment> getCommentsByUser(long userId, long page, long pageSize);
    Map<Long, List<Comment>> getRepliesByFramework(long frameworkId);
    Comment insertComment(long frameworkId, long userId, String description, Long reference);
    int deleteComment(long commentId);
    Optional<Comment> changeComment(long commentId, String description);

}
