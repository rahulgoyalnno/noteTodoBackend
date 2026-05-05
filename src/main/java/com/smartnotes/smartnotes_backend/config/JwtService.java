package com.smartnotes.smartnotes_backend.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    // In production: move this to application.yml + encrypt it
    private static final String SECRET = "smartNotesBackends-super-secret-key-minimum-256-bits!!";

    private static final long EXPIRY = 1000 * 24 * 60 * 60; // 24 hours

    private Key getKey(){
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    public String generateToken(UserDetails user){
        return Jwts.builder()
                .subject(user.getUsername()) // stores email inside them
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRY))
                .signWith(getKey())
                .compact();
    }

    public String extractEmail(String token){
        return Jwts.parser()
                .verifyWith((javax.crypto.SecretKey) getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean isValid(String token, UserDetails user) {
        String email = extractEmail(token);
        return email.equals(user.getUsername())
                && !isExpired(token);
    }

    private boolean isExpired(String token) {
        return Jwts.parser()
                .verifyWith((javax.crypto.SecretKey) getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration()
                .before(new Date());
    }
}
