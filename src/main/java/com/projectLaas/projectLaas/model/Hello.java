package com.projectLaas.projectLaas.model;

import jakarta.persistence.*;

@Entity
@Table(name = "hello")
public class Hello {

      @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
      private Long id;
      private String message;

      // Constructors
      public Hello() {
      }

      public Hello(Long id, String message) {
            this.id = id;
            this.message = message;
      }

      // Getters and Setters
      public Long getId() {
            return id;
      }

      public void setId(Long id) {
            this.id = id;
      }

      public String getMessage() {
            return message;
      }

      public void setMessage(String message) {
            this.message = message;
      }
}