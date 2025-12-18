package com.orioncode.backoffice.databases.objects.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DatabaseObjectResponse {

    private String id;
    private String name;
    private String type;
    private String schemaId;
    private String owner;
    private String description;
    private Double rowCount;
    private Double sizeInKB;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}

