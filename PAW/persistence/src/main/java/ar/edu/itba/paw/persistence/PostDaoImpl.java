package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Post;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class PostDaoImpl implements PostDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<Post> findById(long postId) {
        return Optional.empty();
    }

    @Override
    public List<Post> getAll() {
        return null;
    }

    @Override
    public List<Post> getPostsByUser(long userId, long page, long pageSize) {
        return null;
    }

    @Override
    public Post insertPost(long postId, long userId, String title, String description) {
        return null;
    }

    @Override
    public void deletePost(long postId) {

    }
}
