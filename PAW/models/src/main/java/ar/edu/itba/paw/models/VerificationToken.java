package ar.edu.itba.paw.models;

import java.sql.Timestamp;

public class VerificationToken {
    private Long tokenId;
    private String token;
    private long userId;
    private Timestamp expiryDay;

    public VerificationToken(){
        //For Hibernate
    }

    public VerificationToken(final long tokenId, final String token, final long userId, final Timestamp expiryDay) {
        this.tokenId = tokenId;
        this.token = token;
        this.userId = userId;
        this.expiryDay = expiryDay;
    }

    public VerificationToken( final String token, final long userId, final Timestamp expiryDay) {
        this.token = token;
        this.userId = userId;
        this.expiryDay = expiryDay;
        this.tokenId = null;
    }

    public Long getTokenId() {
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
