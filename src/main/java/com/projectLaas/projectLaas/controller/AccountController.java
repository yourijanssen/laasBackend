package com.projectLaas.projectLaas.controller;

import com.projectLaas.projectLaas.model.User;
import com.projectLaas.projectLaas.modelDTO.*;
import com.projectLaas.projectLaas.service.AccountService;

import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing user accounts.
 */
@RestController
@RequestMapping("/api/accounts")
public class AccountController {

      private final AccountService accountService;

      @Autowired
      public AccountController(AccountService accountService) {
            this.accountService = accountService;
      }

      /**
       * Creates a new user account.
       *
       * @param registerDTO the registration data transfer object containing user
       *                    details
       * @return ResponseEntity containing the created user or error message
       */
      @PreAuthorize("hasRole('TRAINER')")
      @PostMapping("/create")
      public ResponseEntity<?> createAccount(@Valid @RequestBody RegisterDTO registerDTO) {
            try {
                  RegisterDTO createdUser = accountService.createAccount(
                              registerDTO.getName(),
                              registerDTO.getEmail(),
                              registerDTO.getPassword(),
                              registerDTO.getRole());
                  return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
            } catch (IllegalArgumentException e) {
                  return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            }
      }

      /**
       * Retrieves a list of all users.
       *
       * @return ResponseEntity containing a list of all users or an error message.
       */
      @PreAuthorize("hasRole('TRAINER')")
      @GetMapping("/all")
      public ResponseEntity<?> getAllUsers() {
            try {
                  return ResponseEntity.ok(accountService.getAllUsers());
            } catch (Exception e) {
                  return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                              .body("An error occurred while retrieving users.");
            }
      }

      /**
       * Updates user information.
       *
       * @param id        The ID of the user to update.
       * @param updateDTO The data transfer object containing updated user details.
       * @return ResponseEntity containing the updated user or an error message.
       */
      @PreAuthorize("hasRole('TRAINER')")
      @PutMapping("/{id}")
      public ResponseEntity<?> updateUser(@PathVariable Long id, @Valid @RequestBody RegisterDTO updateDTO) {
            try {
                  return ResponseEntity.ok(accountService.updateUser(id, updateDTO));
            } catch (IllegalArgumentException e) {
                  return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
            } catch (Exception e) {
                  return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                              .body("An error occurred while updating the user.");
            }
      }

      /**
       * Deletes a user by ID.
       *
       * @param id The ID of the user to delete.
       * @return ResponseEntity containing a confirmation message or an error message.
       */
      @PreAuthorize("hasRole('TRAINER')")
      @DeleteMapping("/{id}")
      public ResponseEntity<?> deleteUser(@PathVariable Long id) {
            try {
                  accountService.deleteUser(id);
                  return ResponseEntity.ok("User with ID " + id + " has been successfully deleted.");
            } catch (IllegalArgumentException e) {
                  return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
            } catch (Exception e) {
                  return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                              .body("An error occurred while deleting the user.");
            }
      }

      /**
       * Authenticates a user and returns a token.
       *
       * @param loginDTO the login data transfer object containing credentials
       * @return ResponseEntity containing the authentication token or error message
       */

      @PostMapping("/login")
      public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
            try {
                  System.out.println("Login attempt with email: " + loginDTO.getEmail());

                  // Call the authenticateUser method
                  String token = accountService.authenticateUser(loginDTO.getEmail(), loginDTO.getPassword());
                  if (token != null) {
                        System.out.println("Authentication successful for email: " + loginDTO.getEmail());

                        // Fetch the user from the database
                        User user = accountService.getUserByEmail(loginDTO.getEmail());
                        System.out.println("User found: " + user.getName() + ", Role: " + user.getRole());

                        // Create response with token, role, and name
                        Map<String, String> response = new HashMap<>();
                        response.put("token", token);
                        response.put("role", user.getRole());
                        response.put("name", user.getName());

                        return new ResponseEntity<>(response, HttpStatus.OK); // Return the token
                  } else {
                        System.out.println("Authentication failed for email: " + loginDTO.getEmail());
                        return new ResponseEntity<>("Invalid email or password", HttpStatus.UNAUTHORIZED);
                  }
            } catch (Exception e) {
                  System.out.println("An error occurred during login: " + e.getMessage());
                  e.printStackTrace();
                  return new ResponseEntity<>("An error occurred during login", HttpStatus.INTERNAL_SERVER_ERROR);
            }
      }

      /**
       * Handles validation exceptions for HTTP requests.
       *
       * @param ex the exception to handle
       * @return Map containing field names and their corresponding error messages
       *
       * @apiNote Example response:
       *          {
       *          "email": "Invalid email address",
       *          "password": "Password must meet requirements",
       *          "role": "Role must be either TRAINEE or TRAINER"
       *          }
       */
      @ResponseStatus(HttpStatus.BAD_REQUEST)
      @ExceptionHandler({ MethodArgumentNotValidException.class, HttpMessageNotReadableException.class })
      public Map<String, String> handleValidationExceptions(Exception ex) {
            Map<String, String> errors = new HashMap<>();

            ((MethodArgumentNotValidException) ex).getBindingResult()
                        .getFieldErrors()
                        .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

            return errors;
      }

}