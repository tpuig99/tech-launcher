package ar.edu.itba.paw.models;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "verification_token")
public class VerificationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "verification_token_vtoken_id_seq")
    @SequenceGenerator(sequenceName = "verification_token_vtoken_id_seq", name = "verification_token_vtoken_id_seq", allocationSize = 1)
    private Long tokenId;

    @JoinColumn(name = "token",nullable = false)
    private String token;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @JoinColumn(name = "exp_date", nullable = false)
    private Timestamp expiryDay;

    public VerificationToken(){
        //For Hibernate
    }

    public VerificationToken(String token, User user, Timestamp expiryDay) {
        this.token = token;
        this.user = user;
        this.expiryDay = expiryDay;
    }

    public Long getTokenId() {
        return tokenId;
    }

    public String getToken() {
        return token;
    }

    public long getUserId() {
        return user.getId();
    }

    public Timestamp getExpiryDay() {
        return expiryDay;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setTokenId(Long tokenId) {
        this.tokenId = tokenId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setExpiryDay(Timestamp expiryDay) {
        this.expiryDay = expiryDay;
    }
}
