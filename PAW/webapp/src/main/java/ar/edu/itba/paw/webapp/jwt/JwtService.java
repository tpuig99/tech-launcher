package ar.edu.itba.paw.webapp.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class JwtService {

    private static final String BEARER = "Bearer ";
    private static final String USER = "user";
    private static final String ROLES = "roles";
    private static final String ISSUER = "paw-2020b-1";
    private static final String SECRET = "paw-2020b-1-key";

    public static String createToken (String user, List<String> roles) throws UnsupportedEncodingException {
        return JWT.create()
                .withIssuer(ISSUER)
                .withIssuedAt(new Date())
                .withNotBefore(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 3600*1000))
                .withClaim(USER, user)
                .withArrayClaim(ROLES, roles.toArray(new String[0]))
                .sign(Algorithm.HMAC256(SECRET));
    }

    public static boolean isBearer(String authorization) {
        return authorization != null && authorization.startsWith(BEARER) && authorization.split("\\.").length == 3;
    }

    public static String user (String authorization) {
        return verify(authorization).getClaim(USER).asString();
    }

    public static DecodedJWT verify (String authorization) throws JwtException {
        if (!isBearer(authorization)) {
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

    public static List<String> roles (String authorization) throws JwtException {
        return Arrays.asList(verify(authorization).getClaim(ROLES).asArray(String.class));
    }
}
