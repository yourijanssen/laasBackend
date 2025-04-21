package com.projectLaas.projectLaas.util;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Security configuration class for the application.
 * Configures HTTP security, including CORS, CSRF, and JWT-based authentication.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

      /**
       * Configures the security filter chain for the application.
       * 
       * @param http      the {@link HttpSecurity} object used to configure security
       *                  settings
       * @param jwtFilter the custom {@link JwtFilter} for processing JWT tokens
       * @return the configured {@link SecurityFilterChain}
       * @throws Exception if an error occurs during configuration
       */
      @Bean
      public SecurityFilterChain securityFilterChain(HttpSecurity http,
                  @Qualifier("customJwtFilter") JwtFilter jwtFilter) throws Exception {
            http
                        .csrf(csrf -> csrf.disable())
                        .cors(withDefaults())
                        .authorizeHttpRequests(auth -> auth
                                    .requestMatchers("/api/accounts/login", "/api/accounts/create").permitAll()
                                    .requestMatchers("/**").authenticated()
                                    .anyRequest().permitAll())
                        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                        .httpBasic(withDefaults());

            return http.build();
      }
}