package tech.antoniosgarbi.desafioapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.antoniosgarbi.desafioapi.model.UserCustomer;

import java.util.Optional;

public interface UserCustomerRepository extends JpaRepository<UserCustomer, Long> {
    Optional<UserCustomer> findByEmail(String email);
}
