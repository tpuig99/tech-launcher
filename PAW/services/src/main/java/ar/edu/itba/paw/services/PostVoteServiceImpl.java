package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.PostVote;
import ar.edu.itba.paw.persistence.PostVoteDao;
import ar.edu.itba.paw.service.PostVoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PostVoteServiceImpl implements PostVoteService {

    @Autowired
    PostVoteDao postVoteDao;

    private final long PAGE_SIZE = 10;

    @Transactional(readOnly = true)
    @Override
    public Optional<PostVote> getByPost(long postId) {
        return postVoteDao.getByPost(postId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<PostVote> getByUser(long userId, long page) {
        return postVoteDao.getByUser(userId, page, PAGE_SIZE);
    }

    @Override
    public Optional<PostVote> getByPostAndUser(long postId, long userId) {
        return postVoteDao.getByPostAndUser(postId,userId);
    }

    @Transactional
    @Override
    public PostVote insert(long postId, long userId, int vote) {
        Optional<PostVote> postVote = getByPost(postId);
        if(postVote.isPresent()){
            update(postVote.get().getPostVoteId(), vote);
            postVote.get().setVote(vote);
            return postVote.get();
        }
        return postVoteDao.insert(postId, userId, vote);
    }

    @Transactional
    @Override
    public void delete(long postVoteId) {
        postVoteDao.delete(postVoteId);

    }

    @Transactional
    @Override
    public Optional<PostVote> update(long postVoteId, int vote) {
        return postVoteDao.update(postVoteId,vote);
    }
}
