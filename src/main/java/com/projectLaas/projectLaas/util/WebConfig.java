package com.projectLaas.projectLaas.util;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

      @Override
      public void addCorsMappings(CorsRegistry registry) {
            registry.addMapping("/**")
                        .allowedOrigins("http://localhost:4200")
                        .allowedOrigins("https://victorious-tree-009534303.6.azurestaticapps.net/")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
      }

      @Bean
      public FilterRegistrationBean<JwtFilter> jwtFilter() {
            FilterRegistrationBean<JwtFilter> registrationBean = new FilterRegistrationBean<>();
            registrationBean.setFilter(new JwtFilter());
            registrationBean.addUrlPatterns("/api/*"); // Apply to protected endpoints
            return registrationBean;
      }
}