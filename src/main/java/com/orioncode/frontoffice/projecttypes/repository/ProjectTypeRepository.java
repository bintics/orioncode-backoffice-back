package com.orioncode.frontoffice.projecttypes.repository;

import com.orioncode.frontoffice.projecttypes.entity.ProjectType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectTypeRepository extends JpaRepository<ProjectType, String>, JpaSpecificationExecutor<ProjectType> {
    Optional<ProjectType> findByName(String name);
    boolean existsByName(String name);
}

