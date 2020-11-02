package ar.edu.itba.paw.service;

import ar.edu.itba.paw.models.Comment;
import ar.edu.itba.paw.models.PostComment;

import java.util.List;
import java.util.Optional;

public interface PostCommentService {

    Optional<PostComment> findById(long postCommentId);
    List<PostComment> getByPost(long postId);

    PostComment insertPostComment(long postId, long userId, String description, Long reference);
    void deletePostComment(long postCommentId);

    Optional<PostComment> vote(long postCommentId, long userId, int voteSign);

}
