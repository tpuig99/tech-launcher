package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.PostComment;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PostCommentDaoImpl implements PostCommentDao{
    @Override
    public Optional<PostComment> findById(long postCommentId) {
        return Optional.empty();
    }

    @Override
    public List<PostComment> getByPost(long postId) {
        return null;
    }

    @Override
    public PostComment insertPostComment(long postId, long userId, String description, Long reference) {
        return null;
    }

    @Override
    public void deletePostComment(long postCommentId) {

    }
}
