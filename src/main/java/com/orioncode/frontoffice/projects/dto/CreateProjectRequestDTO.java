package com.orioncode.frontoffice.projects.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateProjectRequestDTO {

    @NotBlank(message = "El ID del proyecto es requerido")
    private String id;

    @NotBlank(message = "El nombre del proyecto es requerido")
    private String name;

    @NotBlank(message = "La descripción del proyecto es requerida")
    private String description;

    @NotBlank(message = "El tipo del proyecto es requerido")
    private String typeId;

    @NotBlank(message = "El ID del owner es requerido")
    private String ownerId;

}
