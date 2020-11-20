package ar.edu.itba.paw.service;

import ar.edu.itba.paw.models.Comment;
import ar.edu.itba.paw.models.Post;

import java.util.List;
import java.util.Optional;

public interface PostService {
    Optional<Post> findById(long postId);
    List<Post> getAll(long page, long pageSize);
    List<Post> getPostsByUser(long userId, long page, long pageSize);
    List<Post> getByTagName(String tagName, long page, long pageSize);

    Post insertPost(long userId, String title, String description);
    void deletePost(long postId);

    Optional<Post> vote(long postId, long userId, int voteSign);
    int getPostsAmount();
}
