package com.orioncode.backoffice.team.repository;

import com.orioncode.backoffice.team.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, String> {

    Optional<Team> findByName(String name);

    boolean existsByName(String name);
}

