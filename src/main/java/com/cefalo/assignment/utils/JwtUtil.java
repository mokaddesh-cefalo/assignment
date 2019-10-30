package com.cefalo.assignment.utils;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.function.Function;

public interface JwtUtil {
    String extractUsername(String token);

    Date extractExpiration(String token);

    String extractRole(String token);

    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);

    String generateToken(UserDetails userDetails);

    Boolean validateToken(String token, UserDetails userDetails);
}
