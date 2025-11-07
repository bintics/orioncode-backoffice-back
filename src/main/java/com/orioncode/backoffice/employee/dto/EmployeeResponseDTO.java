package com.orioncode.backoffice.employee.dto;

import com.orioncode.backoffice.position.dto.PositionResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResponseDTO {

    private Long id;
    private String employeeCode;
    private String firstName;
    private String lastName;
    private PositionResponseDTO position;
    private String team;
    private List<String> tags;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
