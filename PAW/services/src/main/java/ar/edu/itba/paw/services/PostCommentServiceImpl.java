package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.PostComment;
import ar.edu.itba.paw.persistence.PostCommentDao;
import ar.edu.itba.paw.service.PostCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostCommentServiceImpl implements PostCommentService {

    @Autowired
    PostCommentDao postCommentDao;

    @Override
    public Optional<PostComment> findById(long postCommentId) {
        return postCommentDao.findById(postCommentId);
    }

    @Override
    public List<PostComment> getByPost(long postId) {
        return postCommentDao.getByPost(postId);
    }

    @Override
    public PostComment insertPostComment(long postId, long userId, String description, Long reference) {
        return postCommentDao.insertPostComment(postId, userId, description, reference);
    }

    @Override
    public void deletePostComment(long postCommentId) {
        postCommentDao.deletePostComment(postCommentId);

    }
}
