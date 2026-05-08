package com.sahil.cardrescue.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import com.sahil.cardrescue.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}