package com.projectLaas.projectLaas.modelDTO;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class LoginDTO {
      @NotBlank(message = "Email is required")
      @Email(message = "Invalid email address")
      private String email;

      @NotBlank(message = "Password is required")
      private String password;

      private String role;
      private String name;

      /**
       * Default constructor.
       */
      public LoginDTO() {
            // Default constructor
      }

      @JsonCreator
      public LoginDTO(
                  @JsonProperty("email") String email,
                  @JsonProperty("password") String password) {
            this.email = email;
            this.password = password;

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

      public void setRole(String password) {
            this.password = password;
      }

      public String getName() {
            return name;
      }

      public void setName(String name) {
            this.name = name;
      }
}
