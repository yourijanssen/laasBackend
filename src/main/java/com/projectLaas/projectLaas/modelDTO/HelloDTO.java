package com.projectLaas.projectLaas.modelDTO;

public class HelloDTO {
      private String message;

      // Constructors
      public HelloDTO() {
      }

      public HelloDTO(String message) {
            this.message = message;
      }

      // Getters and Setters
      public String getMessage() {
            return message;
      }

      public void setMessage(String message) {
            this.message = message;
      }
}