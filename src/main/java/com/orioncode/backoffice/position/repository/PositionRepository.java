package com.orioncode.backoffice.position.repository;

import com.orioncode.backoffice.position.entity.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PositionRepository extends JpaRepository<Position, String> {
    Optional<Position> findByName(String name);
    boolean existsByName(String name);
}
