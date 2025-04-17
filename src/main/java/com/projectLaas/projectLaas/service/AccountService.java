package com.projectLaas.projectLaas.service;

import com.projectLaas.projectLaas.modelDTO.RegisterDTO;
import com.projectLaas.projectLaas.model.User;
import com.projectLaas.projectLaas.repository.AccountRepository;
import com.projectLaas.projectLaas.util.JwtUtil;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Service class for managing user accounts.
 */
@Service
public class AccountService {

      private final AccountRepository accountRepository;
      private final JwtUtil jwtUtil;
      private final BCryptPasswordEncoder passwordEncoder;

      /**
       * Constructor for AccountService.
       *
       * @param accountRepository the repository for accessing user data
       * @param jwtUtil           the utility for generating and validating JWT tokens
       */
      @Autowired
      public AccountService(AccountRepository accountRepository, JwtUtil jwtUtil) {
            this.accountRepository = accountRepository;
            this.jwtUtil = jwtUtil;
            this.passwordEncoder = new BCryptPasswordEncoder();
      }

      /**
       * Retrieves a list of all users.
       *
       * @return a list of RegisterDTO objects representing all users
       */
      public List<RegisterDTO> getAllUsers() {
            return accountRepository.findAll().stream()
                        .map(user -> new RegisterDTO(user.getName(), user.getEmail(), null, user.getRole()))
                        .collect(Collectors.toList());
      }

      /**
       * Updates the details of an existing user.
       *
       * @param id        the ID of the user to update
       * @param updateDTO the updated user details
       * @return a RegisterDTO representing the updated user
       * @throws IllegalArgumentException if the user is not found
       */
      public RegisterDTO updateUser(Long id, RegisterDTO updateDTO) {
            User user = accountRepository.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("User not found"));

            user.setName(updateDTO.getName());
            user.setEmail(updateDTO.getEmail());
            if (updateDTO.getPassword() != null && !updateDTO.getPassword().isEmpty()) {
                  user.setPassword(passwordEncoder.encode(updateDTO.getPassword()));
            }
            user.setRole(updateDTO.getRole());

            User updatedUser = accountRepository.save(user);
            return new RegisterDTO(updatedUser.getName(), updatedUser.getEmail(), null, updatedUser.getRole());
      }

      /**
       * Deletes a user by ID.
       *
       * @param id the ID of the user to delete
       * @throws IllegalArgumentException if the user is not found
       */
      public void deleteUser(Long id) {
            if (!accountRepository.existsById(id)) {
                  throw new IllegalArgumentException("User not found");
            }
            accountRepository.deleteById(id);
      }

      /**
       * Creates a new user account.
       *
       * @param name     the name of the user
       * @param email    the email of the user
       * @param password the password of the user
       * @param role     the role of the user
       * @return a RegisterDTO representing the created user
       * @throws IllegalArgumentException if the email is already in use
       */
      public RegisterDTO createAccount(String name, String email, String password, String role) {
            if (accountRepository.findByEmail(email) != null) {
                  throw new IllegalArgumentException("Email already in use");
            }

            // Hash the password before saving
            String hashedPassword = passwordEncoder.encode(password);
            User user = new User(null, name, email, hashedPassword, role);
            User savedUser = accountRepository.save(user);
            return convertToDTO(savedUser);
      }

      /**
       * Retrieves a user by email.
       *
       * @param email the email of the user to retrieve
       * @return the User entity
       * @throws IllegalArgumentException if the user is not found
       */
      public User getUserByEmail(String email) {
            User user = accountRepository.findByEmail(email);
            if (user != null) {
                  return user;
            }
            throw new IllegalArgumentException("User not found");
      }

      /**
       * Authenticates a user by email and password.
       *
       * @param email    the email of the user
       * @param password the password of the user
       * @return a JWT token if authentication is successful, or null if it fails
       */
      public String authenticateUser(String email, String password) {
            System.out.println("Authenticating user with email: " + email);

            // Fetch the user from the database
            User user = accountRepository.findByEmail(email);
            if (user == null) {
                  System.out.println("No user found with email: " + email);
                  return null; // User not found
            }

            System.out.println("User found: " + user.getName() + ", Role: " + user.getRole());

            // Check if the password matches
            boolean passwordMatches = passwordEncoder.matches(password, user.getPassword());
            System.out.println("Password matches: " + passwordMatches);

            if (passwordMatches) {
                  // Generate and return the JWT token
                  String token = jwtUtil.generateToken(email, user.getRole());
                  System.out.println("Generated JWT token: " + token);
                  return token;
            } else {
                  System.out.println("Password does not match for email: " + email);
                  return null; // Password mismatch
            }
      }

      /**
       * Converts a User entity to a RegisterDTO.
       *
       * @param user the User entity to convert
       * @return a RegisterDTO representing the user
       */
      private RegisterDTO convertToDTO(User user) {
            return new RegisterDTO(user.getName(), user.getEmail(), user.getPassword(), user.getRole());
      }
}