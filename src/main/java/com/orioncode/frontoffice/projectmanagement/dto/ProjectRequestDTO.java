package com.orioncode.frontoffice.projectmanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectRequestDTO {

    @NotBlank(message = "El nombre del proyecto es requerido")
    @Size(min = 2, max = 200, message = "El nombre debe tener entre 2 y 200 caracteres")
    private String name;

    @Size(max = 1000, message = "La descripción no puede exceder 1000 caracteres")
    private String description;

    @NotBlank(message = "El estado es requerido")
    @Size(max = 50, message = "El estado no puede exceder 50 caracteres")
    private String status;

    @NotBlank(message = "El tipo de proyecto es requerido")
    @Size(max = 50, message = "El tipo no puede exceder 50 caracteres")
    private String type;

    @NotBlank(message = "El ID del owner es requerido")
    @Size(max = 100, message = "El ID del owner no puede exceder 100 caracteres")
    private String ownerId;
}

