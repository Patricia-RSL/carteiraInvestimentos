package com.backend.application;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtUtil {

  private final SecretKey secretKey;

  public JwtUtil(@Value("${jwt.secret}") String secret) {
    this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
  }

  private static final long EXPIRATION_TIME = 2592000000L;

  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  public Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);

  }

  private Claims extractAllClaims(String token) {
    return Jwts.parser()
      .verifyWith(secretKey)
      .build()
      .parseSignedClaims(token)
      .getPayload();
  }

  public String generateToken(UserDetails userDetails) {
    return Jwts.builder()
      .subject(userDetails.getUsername())
      .issuer("carteiraInvestimeto")
      .issuedAt(new Date())
      .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
      .signWith(secretKey)
      .compact();
  }

  public Boolean validateToken(String token, UserDetails userDetails) {
    try {
      final String username = extractUsername(token);
      return isTokenValid(token) && username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    } catch (JwtException | IllegalArgumentException e) {
      return false;
    }
  }

  public Boolean isTokenValid(String token) {
    try {
      Jwts.parser()
        .verifyWith(secretKey)
        .build()
        .parseSignedClaims(token);
      return true;
    } catch (JwtException e) {
      return false;
    }
  }

  private Boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

}
