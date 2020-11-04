package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.persistence.PostCommentDao;
import ar.edu.itba.paw.persistence.PostCommentVoteDao;
import ar.edu.itba.paw.service.PostCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostCommentServiceImpl implements PostCommentService {

    @Autowired
    PostCommentDao postCommentDao;

    @Autowired
    PostCommentVoteDao postCommentVoteDao;

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

    @Override
    public Optional<PostComment> vote(long postCommentId, long userId, int voteSign) {
        Optional<PostCommentVote> vote = postCommentVoteDao.getByPostCommentAndUser(postCommentId, userId);
        if (vote.isPresent()) {
            if (vote.get().getVote() == voteSign)
                postCommentVoteDao.delete(vote.get().getPostCommentVoteId());
            else
                postCommentVoteDao.update(vote.get().getPostCommentVoteId(), voteSign);
        } else {
            postCommentVoteDao.insert(postCommentId, userId, voteSign);
        }
        Optional<PostComment> postComment = findById(postCommentId);

        return postComment;
    }
}
