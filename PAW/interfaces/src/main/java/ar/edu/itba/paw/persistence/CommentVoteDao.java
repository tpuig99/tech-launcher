package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.CommentVote;

import java.util.List;
import java.util.Optional;

public interface CommentVoteDao {
    List<CommentVote> getAll();
    List<CommentVote> getByComment(long commentId);
    Optional<CommentVote> getById(long voteId);
    Optional<CommentVote> getByCommentAndUser(long commentId, long userId);
    CommentVote insert(long commentId, long userId, int vote);
    int delete(long voteId);
    Optional<CommentVote> update(long voteId, int vote);
}
