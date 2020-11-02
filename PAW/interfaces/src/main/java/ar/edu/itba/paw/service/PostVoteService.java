package ar.edu.itba.paw.service;

import ar.edu.itba.paw.models.PostVote;

import java.util.List;
import java.util.Optional;

public interface PostVoteService {

    Optional<PostVote> getByPost(long postId);
    List<PostVote> getAllByUser(long userId, long page);

    PostVote insert(long postId, long userId, int vote);
    void delete(long postVoteId);
    Optional<PostVote> update(long postVoteId, int vote);

}
