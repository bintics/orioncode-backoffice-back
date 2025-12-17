package com.orioncode.backoffice.databases.schemas.repository;

import com.orioncode.backoffice.databases.schemas.entity.DatabaseSchema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DatabaseSchemaRepository extends JpaRepository<DatabaseSchema, String>, JpaSpecificationExecutor<DatabaseSchema> {

    List<DatabaseSchema> findByServerId(String serverId);

    List<DatabaseSchema> findByOwner(String owner);

}

