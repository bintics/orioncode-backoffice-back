package com.orioncode.backoffice.databases.servers.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DatabaseServerSearchResponseDTO {

    private String id;
    private String name;
    private String engine;
    private String version;
    private String host;
    private int port;
    private String environment;
    private String description;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}

