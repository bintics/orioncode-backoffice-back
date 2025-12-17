package com.orioncode.frontoffice.projects.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectResponse {

    private String id;
    private String name;
    private String description;
    private String status;
    private String typeId;
    private String ownerId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
