package ar.edu.itba.paw.models;

import javax.persistence.*;

@Entity
@Table(name = "comment_votes")
public class CommentVote {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "comment_votes_vote_id_seq")
    @SequenceGenerator(sequenceName = "comment_votes_vote_id_seq", name = "comment_votes_vote_id_seq", allocationSize = 1)
    @Column(name = "vote_id")
    long commentVoteId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "comment_id", nullable = false)
    Comment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column
    int vote;

    public CommentVote(long commentVoteId, Comment comment, User user, int vote) {

        this.commentVoteId = commentVoteId;
        this.comment = comment;
        this.user = user;
        this.vote = vote;
    }

    public CommentVote() {

    }

    public long getCommentVoteId() {
        return commentVoteId;
    }

    public long getCommentId() {
        return comment.getCommentId();
    }

    public long getUserId() {
        return user.getId();
    }

    public int getVote() {
        return vote;
    }

    public boolean isVoteUp() {
        return vote==1;
    }

}
