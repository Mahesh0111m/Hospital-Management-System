package com.mahesh.HMS.repository;

import com.mahesh.HMS.model.Role;
import com.mahesh.HMS.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    List<User> findAllByRole(Role role);



}

