package com.projectLaas.projectLaas.repository;

import com.projectLaas.projectLaas.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<User, Long> {
      User findByEmail(String email);
}