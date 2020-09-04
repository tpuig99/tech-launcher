package ar.edu.itba.paw.models;

public class Votes {
    long voteId;
    Framework framework;
    User user;

    public Votes(long voteId, Framework framework, User user) {
        this.voteId = voteId;
        this.framework = framework;
        this.user = user;
    }
}
