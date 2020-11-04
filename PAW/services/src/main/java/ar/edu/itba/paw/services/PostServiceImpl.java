package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.persistence.PostDao;
import ar.edu.itba.paw.persistence.PostVoteDao;
import ar.edu.itba.paw.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {

    private final long PAGE_SIZE_USER_PROFILE = 5;

    @Autowired
    private PostDao postDao;

    @Autowired
    private PostVoteDao postVoteDao;

    @Transactional(readOnly = true)
    @Override
    public Optional<Post> findById(long postId) {
        return postDao.findById(postId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Post> getAll(long page, long pageSize) {
        return postDao.getAll(page, pageSize);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Post> getPostsByUser(long userId, long page, long pageSize) {
        return postDao.getPostsByUser(userId, page, pageSize);
    }

    @Transactional
    @Override
    public Post insertPost( long userId, String title, String description) {
        return postDao.insertPost(userId, title, description);
    }

    @Transactional
    @Override
    public void deletePost(long postId) {
        postDao.deletePost(postId);
    }

    @Override
    public Optional<Post> vote(long postId, long userId, int voteSign) {

        Optional<PostVote> vote = postVoteDao.getByPostAndUser(postId,userId);
        if(vote.isPresent()){
            if(vote.get().getVote()==voteSign)
                postVoteDao.delete(vote.get().getPostVoteId());
            else
                postVoteDao.update(vote.get().getPostVoteId(),voteSign);
        }else {
            postVoteDao.insert(postId, userId, voteSign);
        }

        Optional<Post> post = findById(postId);

        return post;
    }
}
