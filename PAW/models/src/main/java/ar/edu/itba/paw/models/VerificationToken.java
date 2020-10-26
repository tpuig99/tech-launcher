package ar.edu.itba.paw.models;

import javax.persistence.*;
import java.sql.Timestamp;

public class VerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "verification_token_vtoken_id_seq")
    @SequenceGenerator(sequenceName = "verification_token_vtoken_id_seq", name = "verification_token_vtoken_id_seq", allocationSize = 1)
    private Long tokenId;

    @JoinColumn(name = "token",nullable = false)
    private String token;

    @JoinColumn(name = "user_id", nullable = false)
    private long userId;

    @JoinColumn(name = "exp_date", nullable = false)
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
