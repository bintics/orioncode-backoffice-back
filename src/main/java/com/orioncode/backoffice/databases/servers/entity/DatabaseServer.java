package com.orioncode.backoffice.databases.servers.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DatabaseServer {

    @Id
    private String id;

    @Column
    private String name;

    @Column
    private String engine; // informix, sql-server, oracle, postgresql

    @Column
    private String version;

    @Column
    private String host;

    @Column
    private int port;

    @Column
    private String environment; // production, staging, qa, development

    @Column
    private String description;

    @Column
    private boolean active = true;

    @Column
    private LocalDateTime createdAt = java.time.LocalDateTime.now();

    @Column
    private LocalDateTime updatedAt = java.time.LocalDateTime.now();
}
