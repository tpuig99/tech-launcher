package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.PostVote;

import java.util.List;
import java.util.Optional;

public interface PostVoteDao {
    Optional<PostVote> getByPost(long postId);
    List<PostVote> getByUser(long userId, long page, long pageSize);
    Optional<PostVote> getByPostAndUser(long postId, long userId);

    PostVote insert(long postId, long userId, int vote);
    void delete(long postVoteId);
    Optional<PostVote> update(long postVoteId, int vote);
}
