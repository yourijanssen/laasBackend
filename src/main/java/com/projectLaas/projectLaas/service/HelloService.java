package com.projectLaas.projectLaas.service;

import com.projectLaas.projectLaas.model.Hello;
import com.projectLaas.projectLaas.modelDTO.HelloDTO;
import com.projectLaas.projectLaas.repository.HelloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class HelloService {

      private final HelloRepository helloRepository;

      @Autowired
      public HelloService(HelloRepository helloRepository) {
            this.helloRepository = helloRepository;
      }

      public HelloDTO getHelloMessage(Long id) {
            Optional<Hello> hello = helloRepository.findById(id);
            return hello.map(h -> new HelloDTO(h.getMessage())).orElse(new HelloDTO("Hello, World!"));
      }
}