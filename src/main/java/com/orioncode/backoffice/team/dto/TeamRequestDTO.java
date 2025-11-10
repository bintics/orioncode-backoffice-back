package com.orioncode.backoffice.team.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamRequestDTO {

    private String id;
    private String name;
    private String description;
}