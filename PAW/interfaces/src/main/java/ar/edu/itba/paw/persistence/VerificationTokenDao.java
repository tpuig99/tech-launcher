package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.VerificationToken;

import java.util.Optional;

public interface VerificationTokenDao {
    void insert(long userId,String token);
    Optional<VerificationToken> getById(long tokenId);
    Optional<VerificationToken> getByUser(long userId);
    int deleteById(long tokenId);
    int deleteByUser(long userId);
    void change(long tokenId, String token);
    Optional<VerificationToken> getByToken(String token);
}
