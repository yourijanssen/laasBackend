package com.projectLaas.projectLaas.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

/**
 * Utility class for managing JSON Web Tokens (JWT).
 * Provides methods for generating, validating, and extracting information from
 * JWT tokens.
 */
@Component
public class JwtUtil {

      private static final String SECRET_KEY = "your-256-bit-secret-your-256-bit-secret"; // Replace with a secure key
      private static final long EXPIRATION_TIME = 86400000; // 1 day in milliseconds

      private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

      /**
       * Generates a JWT token for the given email and role.
       *
       * @param email the email of the user
       * @param role  the role of the user
       * @return a JWT token as a String
       */
      public String generateToken(String email, String role) {
            return Jwts.builder()
                        .setSubject(email)
                        .claim("role", "ROLE_" + role) // Prefix role with "ROLE_"
                        .setIssuedAt(new Date())
                        .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                        .signWith(key, SignatureAlgorithm.HS256)
                        .compact();
      }

      /**
       * Extracts the subject (email) from the JWT token.
       *
       * @param token the JWT token
       * @return the subject (email) as a String
       */
      public String extractSubject(String token) {
            return extractAllClaims(token).getSubject(); // Extract the subject (email) from the token
      }

      /**
       * Extracts the role from the JWT token.
       *
       * @param token the JWT token
       * @return the role as a String
       */
      public String extractRole(String token) {
            return extractAllClaims(token).get("role", String.class);
      }

      /**
       * Extracts all claims from the JWT token.
       *
       * @param token the JWT token
       * @return a Claims object containing all claims in the token
       */
      public Claims extractAllClaims(String token) {
            return Jwts.parserBuilder()
                        .setSigningKey(key) // Use the secret key to validate the token
                        .build()
                        .parseClaimsJws(token)
                        .getBody(); // Extract the claims from the token
      }

      /**
       * Validates the JWT token and extracts the subject (email) if valid.
       *
       * @param token the JWT token
       * @return the subject (email) if the token is valid, or null if invalid
       */
      public String validateToken(String token) {
            try {
                  Claims claims = Jwts.parserBuilder()
                              .setSigningKey(key)
                              .build()
                              .parseClaimsJws(token)
                              .getBody();
                  return claims.getSubject(); // Returns the email
            } catch (JwtException | IllegalArgumentException e) {
                  return null; // Invalid token
            }
      }
}