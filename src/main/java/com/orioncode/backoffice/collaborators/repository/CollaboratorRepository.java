package com.orioncode.backoffice.collaborators.repository;

import com.orioncode.backoffice.collaborators.entity.Collaborator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CollaboratorRepository extends JpaRepository<Collaborator, String>, JpaSpecificationExecutor<Collaborator> {
    List<Collaborator> findByPositionId(String positionId);
}
