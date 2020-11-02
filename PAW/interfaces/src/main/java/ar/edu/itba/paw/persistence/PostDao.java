package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Post;

import java.util.List;
import java.util.Optional;

public interface PostDao {

    Optional<Post> findById(long postId);
    List<Post> getAll(long page, long pageSize);
    List<Post> getPostsByUser(long userId, long page, long pageSize);

    Post insertPost(long userId, String title, String description);
    void deletePost(long postId);

}
