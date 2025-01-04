package com.fintech.service;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTServiceImpl implements JWTService {
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder().setSubject(userDetails.getUsername()).
                setIssuedAt(new Date(System.currentTimeMillis())).
                setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)).
                signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
    }

    public String generateRefreshToken(Map<String, Object> extraClaim, UserDetails userDetails) {
        return Jwts.builder().setClaims(extraClaim).setSubject(userDetails.getUsername()).
                setIssuedAt(new Date(System.currentTimeMillis())).
                setExpiration(new Date(System.currentTimeMillis() + 604800000 )).
                signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
    }


    private <T> T extractClaim(String token, Function<Claims, T> claimsesolver) {
        final Claims claims = extractAllClaims(token);
        return claimsesolver.apply(claims);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
    }

    private Key getSignKey() {
        byte[] key = Decoders.BASE64.decode("413F699798956958744589648759667000t75443568884459898459845978479459");
        return Keys.hmacShaKeyFor(key);
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token){
        return extractClaim(token,Claims::getExpiration).before(new Date());
    }
}
