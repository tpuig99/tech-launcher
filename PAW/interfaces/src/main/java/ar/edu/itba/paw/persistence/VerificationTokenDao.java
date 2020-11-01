package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.VerificationToken;

import java.util.Optional;

public interface VerificationTokenDao {
    void insert(long userId,String token);
    void change(VerificationToken verificationToken, String token);
    Optional<VerificationToken> getByToken(String token);
}
