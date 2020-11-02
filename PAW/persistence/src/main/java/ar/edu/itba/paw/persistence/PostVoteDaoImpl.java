package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.PostVote;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class PostVoteDaoImpl implements PostVoteDao{
    @Override
    public Optional<PostVote> getByPost(long postId) {
        return Optional.empty();
    }

    @Override
    public Optional<PostVote> getAllByUser(long userId, long page, long page_size) {
        return Optional.empty();
    }

    @Override
    public PostVote insert(long postId, long userId, int vote) {
        return null;
    }

    @Override
    public void delete(long postVoteId) {

    }

    @Override
    public Optional<PostVote> update(long postVoteId, int vote) {
        return Optional.empty();
    }
}
