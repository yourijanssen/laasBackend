package com.projectLaas.projectLaas.util;

import com.projectLaas.projectLaas.model.Hello;
import com.projectLaas.projectLaas.repository.HelloRepository;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class HelloInitializer implements CommandLineRunner {

      @Autowired
      private HelloRepository helloRepository;

      @Override
      public void run(String... args) throws Exception {
            // Only initialize if no messages exist
            if (helloRepository.count() == 0) {
                  // Create and save hello messages
                  Hello message1 = new Hello(null, "Hello, World!");
                  Hello message2 = new Hello(null, "Hello, Spring Boot!");
                  Hello message3 = new Hello(null, "Hello, Java!");
                  Hello message4 = new Hello(null, "Hello, Developers!");
                  Hello message5 = new Hello(null, "Hello, Universe!");

                  // Save messages
                  helloRepository.saveAll(Arrays.asList(message1, message2, message3, message4, message5));
            }
      }
}