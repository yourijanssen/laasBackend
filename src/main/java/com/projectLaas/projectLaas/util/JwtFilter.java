package com.projectLaas.projectLaas.util;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.IOException;
import java.util.List;

/**
 * Custom JWT filter for processing incoming requests.
 * This filter validates the JWT token, extracts user details, and sets the
 * authentication context.
 */
@Component("customJwtFilter")
public class JwtFilter extends OncePerRequestFilter {

      @Autowired
      private JwtUtil jwtUtil;

      /**
       * Filters incoming HTTP requests to validate JWT tokens.
       * If a valid token is found, the user's email and role are extracted and added
       * to the security context.
       *
       * @param request     the HTTP request
       * @param response    the HTTP response
       * @param filterChain the filter chain to pass the request/response to the next
       *                    filter
       * @throws ServletException if an error occurs during filtering
       * @throws IOException      if an I/O error occurs during filtering
       */
      @Override
      protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                  throws ServletException, IOException {
            String authorizationHeader = request.getHeader("Authorization");

            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                  String token = authorizationHeader.substring(7);
                  try {
                        String email = jwtUtil.extractSubject(token);
                        String role = jwtUtil.extractRole(token);

                        if (email != null && role != null) {
                              SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role);
                              UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                          email, null, List.of(authority));
                              SecurityContextHolder.getContext().setAuthentication(authentication);
                        }
                  } catch (Exception e) {
                        logger.error("JWT Token validation failed", e);
                  }
            }

            filterChain.doFilter(request, response);
      }
}