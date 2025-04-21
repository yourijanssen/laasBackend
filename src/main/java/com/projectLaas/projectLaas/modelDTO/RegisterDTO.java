package com.projectLaas.projectLaas.modelDTO;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object for User.
 * This class is used to transfer user data between processes.
 */
public class RegisterDTO {
      private Long id;

      @NotBlank(message = "Name is required")
      @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
      private String name;

      @NotBlank(message = "Email is required")
      @Email(message = "Invalid email address")
      private String email;

      @NotBlank(message = "Password is required")
      @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = "Password must be at least 8 characters and contain uppercase, lowercase, number and special character")
      private String password;

      @NotNull(message = "Role is required")
      @Pattern(regexp = "^(TRAINEE|TRAINER)$", message = "Role must be either TRAINEE or TRAINER")
      private String role;

      /**
       * Default constructor.
       */
      public RegisterDTO() {
            // Default constructor
      }

      @JsonCreator
      public RegisterDTO(
                  @JsonProperty("id") Long id,
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