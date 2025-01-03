package com.fintech.service;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;

public interface JWTService {
    String extractUsername(String token);
    String generateToken(UserDetails userDetails);
    String generateRefreshToken(Map<String, Object> extraClaim, UserDetails userDetails);
    boolean isTokenValid(String token, UserDetails userDetails);
}
