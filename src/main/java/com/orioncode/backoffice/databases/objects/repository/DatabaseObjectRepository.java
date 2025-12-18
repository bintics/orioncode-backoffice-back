package com.orioncode.backoffice.databases.objects.repository;

import com.orioncode.backoffice.databases.objects.entity.DatabaseObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DatabaseObjectRepository extends JpaRepository<DatabaseObject, String>, JpaSpecificationExecutor<DatabaseObject> {

    List<DatabaseObject> findBySchemaId(String schemaId);

    List<DatabaseObject> findByType(String type);

    List<DatabaseObject> findByOwner(String owner);

}

