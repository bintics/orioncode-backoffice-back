package com.orioncode.frontoffice.projects.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectSearchResponseDTO {

    private Long id;
    private String name;
    private String description;
    private String status;
    private String type;
    private String ownerId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

