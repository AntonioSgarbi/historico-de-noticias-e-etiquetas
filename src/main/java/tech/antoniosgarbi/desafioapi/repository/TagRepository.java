package tech.antoniosgarbi.desafioapi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import tech.antoniosgarbi.desafioapi.model.Tag;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByValue(String email);

    Page<Tag> findAllByOrderByAccessCountDesc(Pageable pageable);
}
