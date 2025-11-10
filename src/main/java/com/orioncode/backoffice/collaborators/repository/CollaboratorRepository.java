package com.orioncode.backoffice.collaborators.repository;

import com.orioncode.backoffice.collaborators.entity.Collaborator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CollaboratorRepository extends JpaRepository<Collaborator, String> {
    List<Collaborator> findByTeam(String team);
    List<Collaborator> findByPositionId(String positionId);
}
