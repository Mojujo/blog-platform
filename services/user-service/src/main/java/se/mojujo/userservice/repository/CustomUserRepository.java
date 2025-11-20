package se.mojujo.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.mojujo.userservice.user.CustomUser;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomUserRepository extends JpaRepository<CustomUser, UUID> {

    // Method will be called within UserDetailsService
    Optional<CustomUser> findUserByUsername(String username);
    Optional<CustomUser> findUserByEmail(String email);

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

}
