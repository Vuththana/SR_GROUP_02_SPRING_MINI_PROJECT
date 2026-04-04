package org.goros.habit_tracker.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.goros.habit_tracker.model.entity.AppUser;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.function.Function;

@Component
public class JwtService {

    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;
    public static final String SECRET = "FVPr6Q/fVlHGZkElZubC0Zaxv657dPUfDQ4o9DADjSin7+uST1d2A5klMWrMK8fmSl3doyf2wn5zj56VC+qqCg==";

    private String createToken(Map<String, Object> claim, String subject) {
        return Jwts.builder()
                .header()
                .add("typ", "JWT")
                .and()
                .claims(claim)
                .subject(subject)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(getSignKey()).compact();
    }

    private SecretKey getSignKey() {
        byte[] keyBytes = Base64.getDecoder().decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        AppUser appUser = (AppUser) userDetails;

        claims.put("appUserId", appUser.getAppUserId());
        claims.put("email", appUser.getEmail());
        claims.put("username", appUser.getUsername());

        return createToken(claims, appUser.getUsername());
    }

    private Claims extractAllClaim(String token) {
        return Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaim(token);
        return claimsResolver.apply(claims);
    }

    public String extractIdentifier(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpirationDate(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Boolean isTokenExpired(String token) {
        return extractExpirationDate(token).before(new Date());
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String identifier = extractIdentifier(token);
        return (identifier.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
