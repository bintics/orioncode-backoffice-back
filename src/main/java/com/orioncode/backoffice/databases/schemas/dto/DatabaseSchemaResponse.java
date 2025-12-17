package com.orioncode.backoffice.databases.schemas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DatabaseSchemaResponse {

    private String id;
    private String name;
    private String serverId;
    private String description;
    private String owner;
    private Integer tablesCount;
    private Integer viewsCount;
    private Integer proceduresCount;
    private Integer functionsCount;
    private LocalDateTime lastModified;
    private Double sizeInMB;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}

