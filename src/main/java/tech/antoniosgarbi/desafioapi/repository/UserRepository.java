package tech.antoniosgarbi.desafioapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.antoniosgarbi.desafioapi.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
