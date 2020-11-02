package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Post;
import ar.edu.itba.paw.persistence.PostDao;
import ar.edu.itba.paw.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {

    private final long PAGE_SIZE = 10;
    private final long PAGE_SIZE_USER_PROFILE = 5;

    @Autowired
    private PostDao postDao;

    @Transactional(readOnly = true)
    @Override
    public Optional<Post> findById(long postId) {
        return postDao.findById(postId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Post> getAll(long page) {
        return postDao.getAll(page, PAGE_SIZE);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Post> getPostsByUser(long userId, long page) {
        return postDao.getPostsByUser(userId, page, PAGE_SIZE_USER_PROFILE);
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
}
