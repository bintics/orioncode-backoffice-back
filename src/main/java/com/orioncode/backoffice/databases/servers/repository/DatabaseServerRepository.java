package com.orioncode.backoffice.databases.servers.repository;

import com.orioncode.backoffice.databases.servers.entity.DatabaseServer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DatabaseServerRepository extends JpaRepository<DatabaseServer, String>, JpaSpecificationExecutor<DatabaseServer> {

    List<DatabaseServer> findByEnvironment(String environment);

    List<DatabaseServer> findByEngine(String engine);

    List<DatabaseServer> findByActive(boolean active);

}

