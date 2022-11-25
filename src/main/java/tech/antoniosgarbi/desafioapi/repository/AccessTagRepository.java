package tech.antoniosgarbi.desafioapi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import tech.antoniosgarbi.desafioapi.model.AccessTagRegister;
import tech.antoniosgarbi.desafioapi.model.UserCustomer;

public interface AccessTagRepository extends JpaRepository<AccessTagRegister, Long> {
    Page<AccessTagRegister> findAllByUser(UserCustomer user, Pageable pageable);

    void deleteAllByUser(UserCustomer user);

}
