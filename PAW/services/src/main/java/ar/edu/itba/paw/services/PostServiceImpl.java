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

    private final long PAGE_SIZE = 5;

    @Autowired
    private PostDao postDao;

    @Transactional(readOnly = true)
    @Override
    public Optional<Post> findById(long postId) {
        return postDao.findById(postId);
    }

    @Override
    public List<Post> getAll() {
        return null;
    }

    @Override
    public List<Post> getPostsByUser(long userId, long page) {
        return postDao.getPostsByUser(userId, page, PAGE_SIZE);
    }

    @Override
    public Post insertPost( long userId, String title, String description) {
        return postDao.insertPost(userId, title, description);
    }

    @Override
    public void deletePost(long postId) {
        postDao.deletePost(postId);
    }
}
