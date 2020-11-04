package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.*;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public class PostCommentDaoImpl implements PostCommentDao{

    @PersistenceContext
    private EntityManager em;


    @Override
    public Optional<PostComment> findById(long postCommentId) {
        return Optional.ofNullable(em.find(PostComment.class, postCommentId));
    }

    @Override
    public List<PostComment> getByPost(long postId) {
        final TypedQuery<PostComment> query = em.createQuery("from PostComment as pc where pc.post.id = :postId", PostComment.class);
        query.setParameter("postId", postId);
        return query.getResultList();
    }

    @Override
    public PostComment insertPostComment(long postId, long userId, String description, Long reference) {
        final PostComment postComment = new PostComment();
        Date ts = new Date(System.currentTimeMillis());
        postComment.setPost(em.getReference(Post.class, postId));
        postComment.setUser(em.getReference(User.class, userId));
        postComment.setDescription(description);
        postComment.setReference(reference);
        postComment.setTimestamp(ts);
        em.persist(postComment);
        return postComment;

    }

    @Override
    public void deletePostComment(long postCommentId) {
        em.remove(em.getReference(PostComment.class,postCommentId));
    }
}
