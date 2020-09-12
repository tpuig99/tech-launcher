package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.VerificationToken;

public interface VerificationTokenDao {
    VerificationToken insert(long userId,String token);
    VerificationToken getById(long tokenId);
    VerificationToken getByUser(long userId);
    int deleteById(long tokenId);
    int deleteByUser(long userId);
    void change(long tokenId, String token);
    VerificationToken getByToken(String token);
}
