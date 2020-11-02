package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.PostVote;

import java.util.Optional;

public interface PostVoteDao {
    Optional<PostVote> getByPost(long postId);
    Optional<PostVote> getAllByUser(long userId, long page, long page_size);

    PostVote insert(long postId, long userId, int vote);
    void delete(long postVoteId);
    Optional<PostVote> update(long postVoteId, int vote);
}
