package com.orioncode.backoffice.collaborators.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CollaboratorResponseDTO {

    private String id;
    private String firstName;
    private String lastName;
    private String positionId;
    private String teamId;
    private List<String> tags;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
