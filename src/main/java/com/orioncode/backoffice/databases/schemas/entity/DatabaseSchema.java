package com.orioncode.backoffice.databases.schemas.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "database_schema")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DatabaseSchema {
    @Id
    private String id;

    @Column
    private String name;

    @Column
    private String serverId;

    @Column
    private String description;

    @Column
    private String owner;

    @Column
    private Integer tablesCount;

    @Column
    private Integer viewsCount;

    @Column
    private Integer proceduresCount;

    @Column
    private Integer functionsCount;

    @Column
    private LocalDateTime lastModified;

    @Column
    private Double sizeInMB;

    @Column
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column
    private LocalDateTime updatedAt = LocalDateTime.now();
}
