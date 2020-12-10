package ar.edu.itba.paw.webapp.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class JwtService {

    private final String BEARER = "Bearer ";
    private final String USER = "user";
    private final String ROLES = "roles";
    private final String ISSUER = "paw-2020b-1";
    private final String SECRET = "paw-2020b-1-key";

    public String createToken (String user, List<String> roles) throws UnsupportedEncodingException {
        return JWT.create()
                .withIssuer(ISSUER)
                .withIssuedAt(new Date())
                .withNotBefore(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 3600*1000))
                .withClaim(USER, user)
                .withArrayClaim(ROLES, roles.toArray(new String[0]))
                .sign(Algorithm.HMAC256(SECRET));
    }

    public boolean isBearer(String authorization) {
        return authorization != null && authorization.startsWith(BEARER) && authorization.split("\\.").length == 3;
    }

    public String user (String authorization) {
        return this.verify(authorization).getClaim(USER).asString();
    }

    public DecodedJWT verify (String authorization) throws JwtException {
        if (!this.isBearer(authorization)) {
            throw new JwtException("String is not a bearer");
        }
        try {
            return JWT.require(Algorithm.HMAC256(SECRET))
                    .withIssuer(ISSUER).build()
                    .verify(authorization.substring(BEARER.length()));
        } catch (Exception exception) {
            throw new JwtException("JWT is wrong. " + exception.getMessage());
        }

    }

    public List<String> roles (String authorization) throws JwtException {
        return Arrays.asList(this.verify(authorization).getClaim(ROLES).asArray(String.class));
    }
}
