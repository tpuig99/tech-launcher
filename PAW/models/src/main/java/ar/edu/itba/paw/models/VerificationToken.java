package ar.edu.itba.paw.models;

import java.sql.Timestamp;

public class VerificationToken {
    private long tokenId;
    private String token;
    private long userId;
    private Timestamp expiryDay;

    public VerificationToken(long tokenId, String token, long userId, Timestamp expiryDay) {
        this.tokenId = tokenId;
        this.token = token;
        this.userId = userId;
        this.expiryDay = expiryDay;
    }

    public long getTokenId() {
        return tokenId;
    }

    public String getToken() {
        return token;
    }

    public long getUserId() {
        return userId;
    }

    public Timestamp getexpiryDay() {
        return expiryDay;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
