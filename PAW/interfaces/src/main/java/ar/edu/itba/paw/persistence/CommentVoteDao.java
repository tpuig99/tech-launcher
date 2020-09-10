package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.CommentVote;

import java.util.List;

public interface CommentVoteDao {
    List<CommentVote> getAll();
    List<CommentVote> getByComment(long commentId);
    CommentVote getById(long voteId);
    CommentVote getByCommentAndUser(long commentId, long userId);
    CommentVote insert(long commentId, long userId, int vote);
    int delete(long voteId);
    CommentVote update(long voteId, int vote);
}
