package com.projectLaas.projectLaas.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;

/**
 * Entity class representing a user in the system.
 * This class is mapped to the "user_obj" table in the database.
 */
@Entity
@Table(name = "user_obj")
public class User {
      @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
      private Long id;

      @Column(nullable = false)
      private String name;

      @Column(nullable = false, unique = true)
      private String email;

      @Column(nullable = false)
      private String password;

      @Column(nullable = false)
      private String role;

      /**
       * Default constructor for JPA.
       */
      public User() {
            // Default constructor for JPA
      }

      @JsonCreator
      public User(@JsonProperty("id") Long id,
                  @JsonProperty("name") String name,
                  @JsonProperty("email") String email,
                  @JsonProperty("password") String password,
                  @JsonProperty("role") String role) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.password = password;
            this.role = role;
      }

      // Getters and Setters

      public Long getId() {
            return id;
      }

      public void setId(Long id) {
            this.id = id;
      }

      public String getName() {
            return name;
      }

      public void setName(String name) {
            this.name = name;
      }

      public String getEmail() {
            return email;
      }

      public void setEmail(String email) {
            this.email = email;
      }

      public String getPassword() {
            return password;
      }

      public void setPassword(String password) {
            this.password = password;
      }

      public String getRole() {
            return role;
      }

      public void setRole(String role) {
            this.role = role;
      }
}