package com.cosmin.tower_defense_progress_api.security;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;

import java.security.Key;
import java.util.Date;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.springframework.stereotype.Component;

@Component
public class JWTUtil {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }


    public String generateToken(String username){
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000L*60*60*24*365))
                .signWith(getSigningKey(),SignatureAlgorithm.HS256)
                .compact();
    }


    public boolean validateToken(String token,String username){
        String real_usename = extractUsername(token);
        return (real_usename.equals(username) && !isExpired(token));
    }

    private boolean isExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    public String extractUsername(String token){
        return extractAllClaims(token).getSubject();
    }

    private Date extractExpiration(String token){
        return extractAllClaims(token).getExpiration();
    }


    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}
