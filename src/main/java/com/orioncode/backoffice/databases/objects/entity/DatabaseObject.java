package com.orioncode.backoffice.databases.objects.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "database_objects")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DatabaseObject {

    @Id
    private String id;
    @Column
    private String name;
    @Column
    private String type;
    @Column
    private String schemaId;
    @Column
    private String owner;
    @Column
    private String description;
    @Column
    private LocalDateTime createdAt;
    @Column
    private LocalDateTime updatedAt;
    @Column(name="row_count")
    private Double rowCount;
    @Column(name = "size_in_kb")
    private Double sizeInKB;

}
