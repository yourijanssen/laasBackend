package com.projectLaas.projectLaas.util.dataInitializers;

import com.projectLaas.projectLaas.model.User;
import com.projectLaas.projectLaas.repository.AccountRepository;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class UserInitializer implements CommandLineRunner {

      @Autowired
      private AccountRepository accountRepository;

      @Override
      public void run(String... args) throws Exception {
            // Only initialize if no messages exist
            if (accountRepository.count() == 0) {
                  // Create and save hello messages
                  User trainer1 = new User(null, "Youri", "youri@y.nl", "Password123!", "TRAINER");
                  User trainer2 = new User(null, "Trainer Two", "trainer2@y.nl", "Password123!", "TRAINER");
                  User trainer3 = new User(null, "Trainer Three", "trainer3@y.nl", "Password123!", "TRAINER");
                  User trainee1 = new User(null, "Trainee One", "trainee1@y.nl", "Password123!", "TRAINEE");
                  User trainee2 = new User(null, "Trainee Two", "trainee2@y.nl", "Password123!", "TRAINEE");

                  // Save messages
                  accountRepository.saveAll(Arrays.asList(trainer1, trainer2, trainer3, trainee1, trainee2));
            }
      }
}