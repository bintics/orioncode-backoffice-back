package com.orioncode.backoffice.employee.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeRequestDTO {

    @NotBlank(message = "El código de colaborador es requerido")
    @Size(min = 3, max = 50, message = "El código debe tener entre 3 y 50 caracteres")
    private String employeeCode;

    @NotBlank(message = "El nombre es requerido")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String firstName;

    @NotBlank(message = "Los apellidos son requeridos")
    @Size(min = 2, max = 100, message = "Los apellidos deben tener entre 2 y 100 caracteres")
    private String lastName;

    @NotNull(message = "El ID del puesto es requerido")
    private Long positionId;

    @Size(max = 100, message = "El equipo no puede exceder 100 caracteres")
    private String team;

    private List<String> tags;
}
