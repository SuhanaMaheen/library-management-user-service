package edu.library.userservice.repository;

import edu.library.userservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUsersByEmail(String email);
    Optional<User> findUsersByUserId(int userId);
}

