package com.orioncode.backoffice.collaborators.dto;

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
public class CollaboratorRequestDTO {

    private String id;

    @NotBlank(message = "El nombre es requerido")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String firstName;

    @NotBlank(message = "Los apellidos son requeridos")
    @Size(min = 2, max = 100, message = "Los apellidos deben tener entre 2 y 100 caracteres")
    private String lastName;

    @NotNull(message = "El ID del puesto es requerido")
    private String positionId;

    @Size(max = 100, message = "El equipo no puede exceder 100 caracteres")
    private String teamId;

    private List<String> tags;
}
