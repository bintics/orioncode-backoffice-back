package com.orioncode.backoffice.position.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PositionRequestDTO {

    private String id;

    @NotBlank(message = "El nombre del puesto es requerido")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    private String name;

    @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
    private String description;
}
