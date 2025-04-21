package com.projectLaas.projectLaas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.projectLaas.projectLaas.modelDTO.HelloDTO;
import com.projectLaas.projectLaas.service.HelloService;

/**
 * REST controller for handling "Hello" related requests.
 * Provides endpoints for retrieving hello messages.
 */
@RestController
public class HelloController {

      private final HelloService helloService;

      /**
       * Constructor for HelloController.
       *
       * @param helloService the HelloService instance used to handle business logic
       */
      @Autowired
      public HelloController(HelloService helloService) {
            this.helloService = helloService;
      }

      /**
       * Retrieves a hello message for the given ID.
       * Accessible by users with the roles "TRAINER" or "TRAINEE".
       *
       * @param id the ID of the hello message to retrieve
       * @return a HelloDTO containing the hello message
       */
      @PreAuthorize("hasAnyRole('TRAINER', 'TRAINEE')")
      @GetMapping("/hello/{id}")
      public HelloDTO sayHello(@PathVariable Long id) {
            return helloService.getHelloMessage(id);
      }
}