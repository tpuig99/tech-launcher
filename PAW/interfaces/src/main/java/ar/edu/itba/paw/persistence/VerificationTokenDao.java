package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.VerificationToken;

import java.util.Optional;

public interface VerificationTokenDao {
    void insert(long userId,String token);
    Optional<VerificationToken> getById(long tokenId);
    Optional<VerificationToken> getByUser(long userId);
    void change(VerificationToken verificationToken, String token);
    Optional<VerificationToken> getByToken(String token);
}
